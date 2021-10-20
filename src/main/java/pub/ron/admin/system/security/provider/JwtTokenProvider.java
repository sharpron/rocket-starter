package pub.ron.admin.system.security.provider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.time.Duration;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import pub.ron.admin.system.security.principal.UserPrincipal;
import pub.ron.admin.system.security.provider.TokenException.Result;

@Component
@Slf4j
public class JwtTokenProvider implements TokenProvider {

  private static final String COMMA = ",";

  private static final String DEPT_ID_KEY = "dept_id";

  private static final String DEPT_IDS_KEY = "dept_ids";

  private static final String DEPT_PATH_KEY = "dept_path";

  private static final String TOKEN_PREFIX = "tokens:";

  private final Duration accessTokenValidity;

  private final StringRedisTemplate redisTemplate;

  private final JwtParser jwtParser;
  private final Key signKey;

  /**
   * constructor.
   *
   * @param base64Secret        base64Secret
   * @param accessTokenValidity accessTokenValidity
   * @param redisTemplate       redisTemplate
   */
  public JwtTokenProvider(
      @Value("${jwt.base64-secret}") String base64Secret,
      @Value("${jwt.access-token-validity}") Duration accessTokenValidity,
      StringRedisTemplate redisTemplate) {
    this.accessTokenValidity = accessTokenValidity;
    this.redisTemplate = redisTemplate;
    byte[] keyBytes = Decoders.BASE64.decode(base64Secret);
    this.signKey = Keys.hmacShaKeyFor(keyBytes);

    this.jwtParser = Jwts.parserBuilder().setSigningKey(signKey).build();
  }

  @Override
  public String generateToken(UserPrincipal principal) {
    final String jwt = generateJwt(principal, accessTokenValidity);
    redisTemplate.opsForValue().set(getTokenKey(jwt), jwt, accessTokenValidity);
    return jwt;
  }

  @Override
  public UserPrincipal validateToken(String token) throws TokenException {
    try {
      Claims claims = jwtParser.parseClaimsJws(token).getBody();
      redisTemplate.opsForValue().set(getTokenKey(token), token, accessTokenValidity);
      return parseClaims(claims);
    } catch (ExpiredJwtException e) {
      throw new TokenException(
          Result.EXPIRED, String.format("认证过期[%s]", token), parseClaims(e.getClaims()));
    } catch (JwtException e) {
      log.error(e.getMessage());
      throw new TokenException(Result.ILLEGAL, String.format("JWT[%s]已经过期", token), null);
    }
  }

  private String getTokenKey(String token) {
    return TOKEN_PREFIX + token;
  }

  private String generateJwt(UserPrincipal principal, Duration validity) {
    return Jwts.builder()
        .setSubject(principal.getUsername())
        .setId(String.valueOf(principal.getId()))
        .claim(DEPT_PATH_KEY, principal.getDeptPath())
        .claim(DEPT_ID_KEY, principal.getDeptId())
        .claim(
            DEPT_IDS_KEY,
            principal.getDeptIds().stream().map(String::valueOf).collect(Collectors.joining(COMMA)))
        .signWith(signKey, SignatureAlgorithm.HS512)
        .setExpiration(new Date(System.currentTimeMillis() + validity.toMillis()))
        .compact();
  }

  private UserPrincipal parseClaims(Claims claims) {
    final Set<Long> deptIds =
        Arrays.stream(claims.get(DEPT_IDS_KEY).toString().split(COMMA))
            .filter(e -> e.length() > 0)
            .map(Long::valueOf)
            .collect(Collectors.toSet());

    return UserPrincipal.builder()
        .id(Long.valueOf(claims.getId()))
        .username(claims.getSubject())
        .deptId(claims.get(DEPT_ID_KEY, Long.class))
        .deptIds(deptIds)
        .deptPath(claims.get(DEPT_PATH_KEY, String.class))
        .build();
  }

  @Override
  public void clear(String token) {
    redisTemplate.delete(getTokenKey(token));
  }
}
