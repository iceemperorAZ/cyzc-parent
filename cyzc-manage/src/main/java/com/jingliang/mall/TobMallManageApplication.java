package com.jingliang.mall;

import com.citrsw.annatation.ApiConfig;
import com.citrsw.annatation.ApiConfigs;
import com.citrsw.annatation.EnableApi;
import com.jingliang.mall.common.SpringUtils;
import com.jingliang.mall.repository.base.impl.BaseRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 启动类
 *
 * @author Zhenfeng Li
 * @version 1.0
 * @date 2019-09-18 9:41
 */
@SpringBootApplication

@EnableJpaRepositories(repositoryBaseClass = BaseRepositoryImpl.class)
@EnableAsync
@EnableApi
@ApiConfigs(
        underline = false,
        apiConfig = {@ApiConfig(space = "厨亿直采-后端", description = "上海晶粮信息科技公司于2017年注册，公司主营项目:区块链.大数据的应用.智慧社区.新零售“微米铺”是晶粮信息科技依托市政府便民工程响应政府号召，孵化的第 一个便民、惠民、益民项目。我们以“智慧微菜场”的新服务模式进入社区。居民享受便捷的服务，以最短的距离，更快的时间购买到更安全的产品。", email = "15706058532@163.com", title = "厨亿直采-后端",
                website = "https://shanghaijingliang.com", basePackages = {"com.jingliang.mall.controller"})})
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
