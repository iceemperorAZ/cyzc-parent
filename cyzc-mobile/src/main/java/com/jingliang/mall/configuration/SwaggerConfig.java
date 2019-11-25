package com.jingliang.mall.configuration;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger配置
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-19 09:02:33
 */

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Value("${spring.profiles.active}")
    private String active;

    @Bean
    public Docket createBackRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                //在pro[生产]环境下关闭swagger
                .enable(!StringUtils.equals(active, "pro"))
                .groupName("晶粮电商后台接口文档")
                .useDefaultResponseMessages(false)
                .forCodeGeneration(true)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.jingliang.mall.back"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    @Bean
    public Docket createFrontRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                //在pro[生产]环境下关闭swagger
                .enable(!StringUtils.equals(active, "pro"))
                .groupName("晶粮电商前台接口文档")
                .useDefaultResponseMessages(false)
                .forCodeGeneration(true)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.jingliang.mall.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("晶粮电商平台API")
                .description("上海晶粮信息科技公司于2017年注册，公司主营项目:区块链.大数据的应用.智慧社区.新零售“微米铺”是晶粮信息科技依托市政府便民工程响应政府号召，孵化的第 一个便民、惠民、益民项目。我们以“智慧微菜场”的新服务模式进入社区。居民享受便捷的服务，以最短的距离，更快的时间购买到更安全的产品。")
                .contact(new Contact("李振峰", "https://blog.csdn.net/li_habit", "15706058532@163.com"))
                .version("1.0.0")
                .build();
    }
}