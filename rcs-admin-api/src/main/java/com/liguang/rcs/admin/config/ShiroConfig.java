package com.liguang.rcs.admin.config;

import com.liguang.rcs.admin.auth.UserAuthorizingRealm;
import com.liguang.rcs.admin.auth.WebSessionManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.util.LinkedHashMap;
import java.util.Map;
@Configuration
public class ShiroConfig {
    @Bean
    public Realm realm() {
        return new UserAuthorizingRealm();
    }

    @Bean
    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
        filterChainDefinitionMap.put("rcs/auth/login", "anon");
        filterChainDefinitionMap.put("rcs/auth/401", "anon");
        filterChainDefinitionMap.put("rcs/auth/index", "anon");
        filterChainDefinitionMap.put("rcs/auth/403", "anon");
        filterChainDefinitionMap.put("rcs/index/index", "anon");
        filterChainDefinitionMap.put("/swagger-ui.html", "anon");
        //先不用鉴权
        filterChainDefinitionMap.put("rcs/**", "anon");

        filterChainDefinitionMap.put("admin/**", "authc");
        shiroFilterFactoryBean.setLoginUrl("admin/auth/401");
        shiroFilterFactoryBean.setSuccessUrl("admin/auth/index");
        shiroFilterFactoryBean.setUnauthorizedUrl("admin/auth/403");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    @Bean
    public SessionManager sessionManager() {
        WebSessionManager mySessionManager = new WebSessionManager();
        return mySessionManager;
    }

    @Bean
    public DefaultWebSecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(realm());
        securityManager.setSessionManager(sessionManager());
        return securityManager;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public static DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator creator = new DefaultAdvisorAutoProxyCreator();
        creator.setProxyTargetClass(true);
        return creator;
    }
}
