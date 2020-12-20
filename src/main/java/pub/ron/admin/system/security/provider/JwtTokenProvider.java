package pub.ron.admin.system.security.provider;

import pub.ron.admin.system.security.TokenProvider;
import pub.ron.admin.system.security.principal.UserPrincipal;
import io.jsonwebtoken.Claims;
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
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtTokenProvider implements TokenProvider {

  private static final String COMMA = ",";

  private static final String DEPT_ID_KEY = "dept_id";

  private static final String DEPT_IDS_KEY = "dept_ids";

  private static final String DEPT_PATH_KEY = "dept_path";

  private static final String TOKEN_PREFIX = "tokens:";

  private static final String TOKEN_VALUE = "1";

  private final Duration tokenValidity;

  private final Duration tokenValidityForRememberMe;

  private final StringRedisTemplate redisTemplate;

  private final JwtParser jwtParser;
  private final Key signKey;


  public JwtTokenProvider(
      @Value("${jwt.base64-secret}") String base64Secret,
      @Value("${jwt.token-validity}") Duration tokenValidity,
      @Value("${jwt.token-validity-for-remember-me}") Duration tokenValidityForRememberMe,
      StringRedisTemplate redisTemplate) {
    this.tokenValidity = tokenValidity;
    this.tokenValidityForRememberMe = tokenValidityForRememberMe;
    this.redisTemplate = redisTemplate;
    byte[] keyBytes = Decoders.BASE64.decode(base64Secret);
    this.signKey = Keys.hmacShaKeyFor(keyBytes);

    this.jwtParser = Jwts.parserBuilder()
        .setSigningKey(signKey)
        .build();
  }

  @Override
  public String generateToken(UserPrincipal principal, boolean rememberMe) {
    final Duration validity = rememberMe ? tokenValidityForRememberMe : tokenValidity;
    final String jwt = Jwts.builder()
        .setSubject(principal.getUsername())
        .setId(String.valueOf(principal.getId()))
        .claim(DEPT_PATH_KEY, principal.getDeptPath())
        .claim(DEPT_ID_KEY, principal.getDeptId())
        .claim(DEPT_IDS_KEY, principal.getDeptIds().stream()
            .map(String::valueOf)
            .collect(Collectors.joining(COMMA)))
        .signWith(signKey, SignatureAlgorithm.HS512)
        .setExpiration(new Date(System.currentTimeMillis() + validity.toMillis()))
        .compact();
    redisTemplate.opsForValue().set(
        TOKEN_PREFIX + jwt,
        TOKEN_VALUE, validity
    );
    return jwt;
  }

  @Override
  public UserPrincipal validate(String token) {
    if (redisTemplate.hasKey(TOKEN_PREFIX + token) != Boolean.TRUE) {
      throw new UnauthorizedException("jwt过期");
    }
    Claims claims = jwtParser.parseClaimsJws(token).getBody();
    final Set<Long> deptIds = Arrays.stream(
        claims.get(DEPT_IDS_KEY).toString().split(COMMA))
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
    redisTemplate.delete(TOKEN_PREFIX + token);
  }
}
