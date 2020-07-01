package com.jingliang.mall.configuration;

import com.jingliang.mall.interceptor.PassWordCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * 拦截器配置
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-19 09:02:33
 */
@Configuration
public class InterceptorConfig extends WebMvcConfigurationSupport {

    private final PassWordCheckInterceptor passWordCheckInterceptor;

    public InterceptorConfig(PassWordCheckInterceptor passWordCheckInterceptor) {
        this.passWordCheckInterceptor = passWordCheckInterceptor;
    }


    /**
     * 放行swagger资源文件
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //新版API
        registry.addResourceHandler("/smart/**").addResourceLocations("classpath:/api/");
        registry.addResourceHandler("/smart/api/**").addResourceLocations("classpath:/api/");
        super.addResourceHandlers(registry);
    }

    /**
     * 配置拦截器执行顺序
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 多个拦截器组成一个拦截器链
        // 默认全部拦截
        // addPathPatterns 添加拦截
        // excludePathPatterns 排除拦截
        //密码检查拦截器 检查密码是否符合规范
        registry.addInterceptor(passWordCheckInterceptor)
                //需要拦截的uri
                .addPathPatterns("/**")
                //需要跳过的uri
                .excludePathPatterns("/back/user/modify/password")
                //拦截器的执行顺序 设置高一点方便后期扩展
                .order(0);
        super.addInterceptors(registry);
    }
}