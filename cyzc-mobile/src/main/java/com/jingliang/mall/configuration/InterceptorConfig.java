package com.jingliang.mall.configuration;

import com.jingliang.mall.interceptor.FrontLoginInterceptor;
import com.jingliang.mall.interceptor.WechatBossInterceptor;
import com.jingliang.mall.interceptor.WechatInterceptor;
import com.jingliang.mall.interceptor.WechatManagerInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * 拦截器配置
 * <br/>
 * Created in 2019-02-15 14:02
 *
 * @author Li Zhenfeng
 */
@Configuration
public class InterceptorConfig extends WebMvcConfigurationSupport {

    private final FrontLoginInterceptor frontLoginInterceptor;
    private final WechatBossInterceptor wechatBossInterceptor;
    private final WechatManagerInterceptor wechatManagerInterceptor;
    private final WechatInterceptor wechatInterceptor;

    public InterceptorConfig(FrontLoginInterceptor frontLoginInterceptor, WechatBossInterceptor wechatBossInterceptor, WechatManagerInterceptor wechatManagerInterceptor, WechatInterceptor wechatInterceptor) {
        this.frontLoginInterceptor = frontLoginInterceptor;
        this.wechatBossInterceptor = wechatBossInterceptor;
        this.wechatManagerInterceptor = wechatManagerInterceptor;
        this.wechatInterceptor = wechatInterceptor;
    }

    /**
     * 放行swagger资源文件
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
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
        //前台拦截器
        registry.addInterceptor(frontLoginInterceptor)
                //需要拦截的uri
                .addPathPatterns("/front/**")
                //需要跳过的uri
                .excludePathPatterns("/front/buyer/wechat/login", "/front/buyer/logout", "/front/pay/wechat/notify","/front/config/minimum"
                        ,/* "/front/productType/**", "/front/product/**",*/ "/front/region/**")
                //拦截器的执行顺序 设置高一点方便后期扩展
                .order(1);
        //前台拦截器
        registry.addInterceptor(wechatBossInterceptor)
                //需要拦截的uri
                .addPathPatterns("/wx/boss/**")
                //拦截器的执行顺序 设置高一点方便后期扩展
                .order(1);
        //前台拦截器
        registry.addInterceptor(wechatManagerInterceptor)
                //需要拦截的uri
                .addPathPatterns("/wx/manager/**")
                //拦截器的执行顺序 设置高一点方便后期扩展
                .order(1);
        //前台拦截器
        registry.addInterceptor(wechatInterceptor)
                //需要拦截的uri
                .addPathPatterns("/wx/**")
                //需要跳过的uri
                .excludePathPatterns("/wx/manager/**","/wx/boss/**")
                //拦截器的执行顺序 设置高一点方便后期扩展
                .order(1);
        super.addInterceptors(registry);
    }
}