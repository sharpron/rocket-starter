package pub.ron.admin.config;

import pub.ron.admin.system.repo.RoleRepo;
import pub.ron.admin.system.repo.UserRepo;
import pub.ron.admin.system.security.JWTFilter;
import pub.ron.admin.system.security.TokenProvider;
import pub.ron.admin.system.security.UseOneRealmAuthenticator;
import pub.ron.admin.system.security.realm.JwtRealm;
import pub.ron.admin.system.security.realm.UserRealm;
import pub.ron.admin.system.service.MenuService;
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

/**
 * @author ron 2020/12/17
 */
@Configuration
public class SecurityConfig {

  private static final String JWT_NAME = "jwt";

  @Bean
  public Realm realm(
      TokenProvider tokenProvider,
      MenuService menuService) {
    return new JwtRealm(tokenProvider, menuService);
  }

  @Bean
  public Realm userRealm(
      UserRepo userRepo, RoleRepo roleRepo,
      CredentialsMatcher credentialsMatcher) {
    final UserRealm userRealm = new UserRealm(userRepo, roleRepo);
    userRealm.setCredentialsMatcher(credentialsMatcher);
    return userRealm;
  }

  @Bean
  public Authenticator authenticator() {
    return new UseOneRealmAuthenticator();
  }

  @Bean
  public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
    ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
    // 设置 securityManager
    SecurityUtils.setSecurityManager(securityManager);
    shiroFilterFactoryBean.setSecurityManager(securityManager);

    // 在 Shiro过滤器链上加入 自定义过滤器JWTFilter 并取名为jwt
    Map<String, Filter> filters = new LinkedHashMap<>();
    filters.put(JWT_NAME, new JWTFilter());
    shiroFilterFactoryBean.setFilters(filters);

    // 自定义url规则
    LinkedHashMap<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
    // 所有请求都要经过 jwt过滤器
    filterChainDefinitionMap.put("/api/authenticate", "anon");
    filterChainDefinitionMap.put("/api/captchas", "anon");
    filterChainDefinitionMap.put("/api/**", "jwt");
    shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
    return shiroFilterFactoryBean;
  }

  @Bean
  @DependsOn({"lifecycleBeanPostProcessor"})
  public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
    // 设置代理类
    DefaultAdvisorAutoProxyCreator creator = new DefaultAdvisorAutoProxyCreator();
    creator.setProxyTargetClass(true);
    return creator;
  }

  @Bean("authorizationAttributeSourceAdvisor")
  public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(
      SecurityManager securityManager) {
    AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
    authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
    return authorizationAttributeSourceAdvisor;
  }
}
