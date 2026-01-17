package rocket.starter.config;

import java.util.LinkedHashMap;
import javax.servlet.Filter;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import rocket.starter.system.security.RestFormAuthenticationFilter;
import rocket.starter.system.security.UserLocker;
import rocket.starter.system.security.UserRealm;
import rocket.starter.system.service.MenuService;
import rocket.starter.system.service.RoleService;
import rocket.starter.system.service.UserService;

/**
 * project security config.
 *
 * @author ron 2020/12/17
 */
@Configuration
public class SecurityConfig {

  /**
   * authenticate realm.
   *
   * @param userService        userService
   * @param menuService        menuService
   * @param roleService        roleService
   * @param userLocker         userLocker
   * @param credentialsMatcher credentials matcher
   * @return realm
   */
  @Bean
  public Realm userRealm(
      UserService userService, MenuService menuService,
      RoleService roleService,
      UserLocker userLocker,
      CredentialsMatcher credentialsMatcher) {
    final UserRealm userRealm = new UserRealm(userService,
        menuService, roleService, userLocker);
    userRealm.setCredentialsMatcher(credentialsMatcher);
    return userRealm;
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
    shiroFilterFactoryBean.setLoginUrl(null);
    shiroFilterFactoryBean.setSuccessUrl(null);
    shiroFilterFactoryBean.setUnauthorizedUrl(null);

    // 自定义url规则
    LinkedHashMap<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
    // 所有请求都要经过 jwt过滤器
    filterChainDefinitionMap.put("/api/authenticate", "anon");
    filterChainDefinitionMap.put("/api/is-authenticated", "anon");
    filterChainDefinitionMap.put("/api/captcha", "anon");

    filterChainDefinitionMap.put("/api/**", "authc");
    shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

    LinkedHashMap<String, Filter> filterLinkedHashMap = new LinkedHashMap<>();
    filterLinkedHashMap.put("authc", new RestFormAuthenticationFilter());
    shiroFilterFactoryBean.setFilters(filterLinkedHashMap);
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
