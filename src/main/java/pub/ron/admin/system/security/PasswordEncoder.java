package pub.ron.admin.system.security;

/**
 * @author ron 2020/12/17
 */
public interface PasswordEncoder {

  String encoded(String password, String salt);
}
