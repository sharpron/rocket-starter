package pub.ron.admin.config;

import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.Filter;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.Authenticator;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import pub.ron.admin.system.repo.RoleRepo;
import pub.ron.admin.system.repo.UserRepo;
import pub.ron.admin.system.security.JwtFilter;
import pub.ron.admin.system.security.UseOneRealmAuthenticator;
import pub.ron.admin.system.security.provider.TokenProvider;
import pub.ron.admin.system.security.realm.JwtRealm;
import pub.ron.admin.system.security.realm.UserRealm;
import pub.ron.admin.system.service.MenuService;

/**
 * project security config.
 *
 * @author ron 2020/12/17
 */
@Configuration
public class SecurityConfig {

  private static final String JWT_NAME = "jwt";

  /**
   * json web token realm for shiro.
   *
   * @param tokenProvider token provider
   * @param menuService menu service
   * @return realm
   */
  @Bean
  public Realm jwtRealm(TokenProvider tokenProvider, MenuService menuService) {
    return new JwtRealm(tokenProvider, menuService);
  }

  /**
   * authenticate realm.
   *
   * @param userRepo user repository
   * @param roleRepo role repository
   * @param credentialsMatcher credentials matcher
   * @return realm
   */
  @Bean
  public Realm userRealm(
      UserRepo userRepo, RoleRepo roleRepo, CredentialsMatcher credentialsMatcher) {
    final UserRealm userRealm = new UserRealm(userRepo, roleRepo);
    userRealm.setCredentialsMatcher(credentialsMatcher);
    return userRealm;
  }

  @Bean
  public Authenticator authenticator() {
    return new UseOneRealmAuthenticator();
  }

  /**
   * shiro filter factory config.
   *
   * @param securityManager securityManager
   * @return factory
   */
  @Bean
  public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
    ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
    // 设置 securityManager
    SecurityUtils.setSecurityManager(securityManager);
    shiroFilterFactoryBean.setSecurityManager(securityManager);

    // 在 Shiro过滤器链上加入 自定义过滤器JWTFilter 并取名为jwt
    Map<String, Filter> filters = new LinkedHashMap<>();
    filters.put(JWT_NAME, new JwtFilter());
    shiroFilterFactoryBean.setFilters(filters);

    // 自定义url规则
    LinkedHashMap<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
    // 所有请求都要经过 jwt过滤器
    filterChainDefinitionMap.put("/api/authenticate", "anon");
    filterChainDefinitionMap.put("/api/captcha", "anon");
    filterChainDefinitionMap.put("/api/**", "jwt");
    shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
    return shiroFilterFactoryBean;
  }

  /**
   * default advisor auto proxy.
   *
   * @return proxy creator
   */
  @Bean
  @DependsOn({"lifecycleBeanPostProcessor"})
  public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
    // 设置代理类
    DefaultAdvisorAutoProxyCreator creator = new DefaultAdvisorAutoProxyCreator();
    creator.setProxyTargetClass(true);
    return creator;
  }

  /**
   * annotation supports.
   *
   * @param securityManager securityManager
   * @return advisor
   */
  @Bean("authorizationAttributeSourceAdvisor")
  public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(
      SecurityManager securityManager) {
    AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor =
        new AuthorizationAttributeSourceAdvisor();
    authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
    return authorizationAttributeSourceAdvisor;
  }
}
