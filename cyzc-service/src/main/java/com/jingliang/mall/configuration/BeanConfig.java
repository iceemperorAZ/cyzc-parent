package com.jingliang.mall.configuration;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.thymeleaf.expression.Maps;

import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Bean配置
 *
 * @author Zhenfeng Li
 * @version 1.0
 * @date 2019-09-21 12:16
 */
@Configuration
public class BeanConfig {
    private final ConfigurableEnvironment env;

    public BeanConfig(ConfigurableEnvironment env) {
        this.env = env;
    }

    /**
     * redis 配置
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        // key采用String的序列化方式
        template.setKeySerializer(stringRedisSerializer);
        // hash的key也采用String的序列化方式
        template.setHashKeySerializer(stringRedisSerializer);
        // value序列化方式采用jackson
        template.setValueSerializer(jackson2JsonRedisSerializer);
        // hash的value序列化方式采用jackson
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }

    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        return container;
    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        List<MediaType> mediaTypes = new ArrayList<>();
        mediaTypes.add(MediaType.TEXT_PLAIN);
        mediaTypes.add(MediaType.TEXT_XML);
        mediaTypes.add(MediaType.APPLICATION_XML);
        mappingJackson2HttpMessageConverter.setSupportedMediaTypes(mediaTypes);
        restTemplate.getMessageConverters().add(mappingJackson2HttpMessageConverter);
        List<HttpMessageConverter<?>> list = restTemplate.getMessageConverters();
        for (HttpMessageConverter<?> httpMessageConverter : list) {
            if (httpMessageConverter instanceof StringHttpMessageConverter) {
                ((StringHttpMessageConverter) httpMessageConverter).setDefaultCharset(StandardCharsets.UTF_8);
                break;
            }
        }
        return restTemplate;
    }

    /**
     * 构建下单成功之后-订单超时未支付的死信队列消息模型
     */
    @Bean
    public Queue orderQueue() {
        Map<String, Object> argsMap = new LinkedHashMap<>();
        argsMap.put("x-dead-letter-exchange", env.getProperty("rabbitmq.order.pay.dead.exchange"));
        argsMap.put("x-dead-letter-routing-key", env.getProperty("rabbitmq.order.pay.dead.routing.key"));
        return new Queue(Objects.requireNonNull(env.getProperty("rabbitmq.order.pay.dead.queue")), true, false, false, argsMap);
    }

    /**
     * 基本交换机
     */
    @Bean
    public TopicExchange basicExchange() {
        return new TopicExchange(env.getProperty("rabbitmq.basic.exchange"), true, false);
    }

    /**
     * 创建基本交换机+订单路由 -> 死信队列 的绑定
     */
    @Bean
    public Binding orderDeadProdBinding() {
        return BindingBuilder.bind(orderQueue()).to(basicExchange()).with(env.getProperty("rabbitmq.order.pay.dead.prod.routing.key"));
    }

    /**
     * 订单预支付的队列
     */
    @Bean
    public Queue orderRealQueue() {
        return new Queue(Objects.requireNonNull(env.getProperty("rabbitmq.order.pay.dead.real.queue")), true);
    }

    /**
     * 订单预支付死信交换机
     */
    @Bean
    public TopicExchange orderDeadExchange() {
        return new TopicExchange(env.getProperty("rabbitmq.order.pay.dead.exchange"), true, false);
    }

    /**
     * 死信交换机+死信路由-> 订单预支付队列 的绑定
     */
    @Bean
    public Binding orderDeadBinding() {
        return BindingBuilder.bind(orderRealQueue()).to(orderDeadExchange()).with(env.getProperty("rabbitmq.order.pay.dead.routing.key"));
    }

    /**
     * 单一消费者
     */
    @Bean(name = "singleListenerContainer")
    public SimpleRabbitListenerContainerFactory listenerContainer(CachingConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        factory.setConcurrentConsumers(1);
        factory.setMaxConcurrentConsumers(1);
        factory.setPrefetchCount(1);
        factory.setTxSize(1);
        return factory;
    }

    /**
     * 库存消息队列
     */
    @Bean
    public Queue skuQueue() {
        Map<String, Object> argsMap =new LinkedHashMap<>();
        argsMap.put("x-dead-letter-exchange", env.getProperty("rabbitmq.order.pay.dead.exchange"));
        argsMap.put("x-dead-letter-routing-key", env.getProperty("rabbitmq.sku.routing.key"));
        return new Queue(Objects.requireNonNull(env.getProperty("rabbitmq.sku.queue")), true, false, false, argsMap);
    }

    /**
     * 创建基本交换机+库存路由->库存队列 的绑定
     */
    @Bean
    public Binding skuBinding() {
        return BindingBuilder.bind(skuQueue()).to(basicExchange()).with(env.getProperty("rabbitmq.sku.routing.key"));
    }


    /**
     * 优惠券消息队列
     */
    @Bean
    public Queue couponQueue() {
        Map<String, Object> argsMap = new LinkedHashMap<>();
        argsMap.put("x-dead-letter-exchange", env.getProperty("rabbitmq.order.pay.dead.exchange"));
        argsMap.put("x-dead-letter-routing-key", env.getProperty("rabbitmq.coupon.routing.key"));
        return new Queue(Objects.requireNonNull(env.getProperty("rabbitmq.coupon.queue")), true, false, false, argsMap);
    }

    /**
     * 创建基本交换机+优惠券路由->优惠券队列 的绑定
     */
    @Bean
    public Binding couponBinding() {
        return BindingBuilder.bind(couponQueue()).to(basicExchange()).with(env.getProperty("rabbitmq.coupon.routing.key"));
    }

    /**
     * 订单支付通知交换机
     */
    @Bean
    public FanoutExchange paymentNoticeExchange() {
        return new FanoutExchange(env.getProperty("rabbitmq.payment.notice.exchange"), true, false);
    }

    /**
     * 订单支付通知消息队列（以广播的形式）
     */
    @Bean
    public Queue paymentNoticeQueue() {
        return new Queue(Objects.requireNonNull(env.getProperty("rabbitmq.payment.notice.queue")));
    }

    /**
     * 创建基本交换机+订单支付通知队列 的绑定（以广播的形式）
     */
    @Bean
    public Binding paymentNoticeBinding() {
        return BindingBuilder.bind(paymentNoticeQueue()).to(paymentNoticeExchange());
    }
}
