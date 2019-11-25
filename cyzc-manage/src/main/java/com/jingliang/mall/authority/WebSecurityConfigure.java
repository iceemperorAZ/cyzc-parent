/**
 * Project Name:auth2
 * File Name:WebSecurityConfig.java
 * Package Name:com.briup.apps.auth2.config
 * Date:2018年9月17日上午10:23:44
 * Copyright (c) 2018, chenzhou1025@126.com All Rights Reserved.
 */

package com.jingliang.mall.authority;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * SpringSecurity配置类
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-11-01 15:13:01
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfigure extends WebSecurityConfigurerAdapter {
    private final MallAuthenticationProvider mallAuthenticationProvider;
    /**
     * 自定义无权访问处理
     */
    private final MallFilterInvocationSecurityMetadataSource filterInvocationSecurityMetadataSource;
    /**
     * 自定义权限判断
     */
    private final MallAccessDecisionManager accessDecisionManager;
    /**
     * 自定义未登录时JSON数据
     */
    private final UrlAuthenticationEntryPoint authenticationEntryPoint;
    /**
     * 自定义注销成功处理器
     */
    private final UrlLogoutSuccessHandler logoutSuccessHandler;
    /**
     * 自定义无权访问处理
     */
    private final UrlAccessDeniedHandler accessDeniedHandler;
    /**
     * 自定义登录成功处理器
     */
    private final UrlLoginSuccessHandler urlLoginSuccessHandler;
    /**
     * 自定义登录失败处理器
     */
    private final UrlLoginFailHandler urlLoginFailHandler;
    /**
     * 自定义token过滤器
     */
    private final JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;


    public WebSecurityConfigure(MallAuthenticationProvider mallAuthenticationProvider, MallFilterInvocationSecurityMetadataSource filterInvocationSecurityMetadataSource
            , MallAccessDecisionManager accessDecisionManager, UrlAuthenticationEntryPoint authenticationEntryPoint, UrlLogoutSuccessHandler logoutSuccessHandler
            , UrlAccessDeniedHandler accessDeniedHandler, UrlLoginSuccessHandler urlLoginSuccessHandler, UrlLoginFailHandler urlLoginFailHandler
            , JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter) {
        this.mallAuthenticationProvider = mallAuthenticationProvider;
        this.filterInvocationSecurityMetadataSource = filterInvocationSecurityMetadataSource;
        this.accessDecisionManager = accessDecisionManager;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.logoutSuccessHandler = logoutSuccessHandler;
        this.accessDeniedHandler = accessDeniedHandler;
        this.urlLoginSuccessHandler = urlLoginSuccessHandler;
        this.urlLoginFailHandler = urlLoginFailHandler;
        this.jwtAuthenticationTokenFilter = jwtAuthenticationTokenFilter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        //自定义登录认证
        auth.authenticationProvider(mallAuthenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //开启登录功能
                .formLogin().permitAll()
                .and()
                //开启注销功能
                .logout()
                //自定义注销请求路径
                .logoutUrl("/back/user/logout")
                //注销成功处理器(前后端分离) ,当session被禁用后onLogoutSuccess的参数(Authentication)将无法获取到值
                .logoutSuccessHandler(logoutSuccessHandler)
                .permitAll()
                .and()
                //使用jwt，不需要csrf
                .csrf().disable()
                //基于token，不需要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                //设置允许访问的资源[swagger2]
                .antMatchers(
                        "/v2/api-docs",
                        "/swagger-resources",
                        "/swagger-resources/**",
                        "/configuration/ui",
                        "/configuration/security",
                        "/swagger-ui.html/**",
                        "/webjars/**"

                ).permitAll()
                //其他全部拦截
                .anyRequest().authenticated()
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O o) {
                        //动态获取url权限配置
                        o.setSecurityMetadataSource(filterInvocationSecurityMetadataSource);
                        //权限判断
                        o.setAccessDecisionManager(accessDecisionManager);
                        return o;
                    }
                })
                //无权访问异常处理器
                .and().exceptionHandling().accessDeniedHandler(accessDeniedHandler)
                //用户未登录处理
                .authenticationEntryPoint(authenticationEntryPoint);
        // 禁用缓存
        http.headers().cacheControl();
        //添加登录 filter
        http.addFilterBefore(customAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        // 添加JWT filter
        http.addFilterAt(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    JsonLoginAuthenticationFilter customAuthenticationFilter() throws Exception {
        JsonLoginAuthenticationFilter filter = new JsonLoginAuthenticationFilter();
        //成功处理
        filter.setAuthenticationSuccessHandler(urlLoginSuccessHandler);
        //失败处理
        filter.setAuthenticationFailureHandler(urlLoginFailHandler);
        //拦截路径
        filter.setFilterProcessesUrl("/back/user/login");
        //这句很关键，重用WebSecurityConfigurerAdapter配置的AuthenticationManager，不然要自己组装AuthenticationManager
        filter.setAuthenticationManager(authenticationManagerBean());
        return filter;
    }

}
