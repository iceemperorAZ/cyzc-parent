package com.jingliang.mall;

import com.jingliang.mall.common.SpringUtils;
import com.jingliang.mall.repository.base.impl.BaseRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * 启动类
 *
 * @author Zhenfeng Li
 * @version 1.0
 * @date 2019-09-18 9:41
 */
@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass = BaseRepositoryImpl.class)
public class TobMallManageApplication {
    /**
     * 使用这个main类启动请在启动参数中增加如下配置(VM options中)
     * 开发环境-Dspring.profiles.active=dev
     * 测试环境-Dspring.profiles.active=test
     * 生产环境-Dspring.profiles.active=pro
     * 直接运行编译后的jar请在启动参数中增加如下配置
     * 开发环境-Dspring.profiles.active=dev
     * 测试环境-Dspring.profiles.active=test
     * 生产环境-Dspring.profiles.active=pro
     */
    public static void main(String[] args) {
        //同时集成redis和elasticsearch会报异常加入此配置解决
        System.setProperty("es.set.netty.runtime.available.processors", "false");
        ConfigurableApplicationContext applicationContext = SpringApplication.run(TobMallManageApplication.class, args);
        //保存spring上下文到spring工具类
        SpringUtils.setApplicationContext(applicationContext);
    }
}
