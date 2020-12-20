package pub.ron.admin.system.security;

import java.util.UUID;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.stereotype.Component;

/**
 * @author ron 2020/12/17
 */
@Component
public class PasswordEncoderImpl implements PasswordEncoder, CredentialsMatcher {

  private static final int HASH_ITERATIONS = 1024;
  private static final String HASH_ALGORITHM_NAME = Sha256Hash.ALGORITHM_NAME;
  private final CredentialsMatcher credentialsMatcher;

  public PasswordEncoderImpl() {
    HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
    credentialsMatcher.setHashAlgorithmName(HASH_ALGORITHM_NAME);
    credentialsMatcher.setHashIterations(HASH_ITERATIONS);
    this.credentialsMatcher = credentialsMatcher;
  }

  @Override
  public String encoded(String password, String salt) {
    return new SimpleHash(HASH_ALGORITHM_NAME, password, salt, HASH_ITERATIONS).toHex();
  }

  @Override
  public boolean doCredentialsMatch(AuthenticationToken authenticationToken,
      AuthenticationInfo authenticationInfo) {
    return credentialsMatcher.doCredentialsMatch(authenticationToken, authenticationInfo);
  }

  public static void main(String[] args) {
    final String s = UUID.randomUUID().toString();
    final String admin123 = new PasswordEncoderImpl().encoded("admin123", s);
    System.out.println(s);
    System.out.println(admin123);
  }
}
