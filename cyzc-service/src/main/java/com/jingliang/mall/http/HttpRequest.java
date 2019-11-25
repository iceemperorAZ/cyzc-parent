package com.jingliang.mall.http;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * 网络请求
 *
 * @author Zhenfeng Li
 * @version 1.0
 * @date 2019-10-23 13:06
 */
@Component
@Slf4j
public class HttpRequest {
    private final RestTemplate restTemplate;

    public HttpRequest(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * 发送get请求
     *
     * @param url    请求地址
     * @param aClass 返回类型
     * @return 返回ResponseEntity
     */
    public <T> ResponseEntity<T> get(String url, Class<T> aClass) {
        return restTemplate.getForEntity(url, aClass);
    }
}
