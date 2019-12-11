package com.jingliang.mall.websocket;

import com.jingliang.mall.entity.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * websocket服务
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-24 16:53:10
 */
@ServerEndpoint(value = "/ws/cyzc")
@Component
@Slf4j
public class WebSocketServer {

    /**
     * 连接计数器
     */
    private static final AtomicInteger ONLINE_COUNT = new AtomicInteger(0);
    /**
     * concurrent包的线程安全Set，用来存放每个客户端对应的Session对象。
     */
    private static CopyOnWriteArraySet<Session> sessionSet = new CopyOnWriteArraySet<Session>();


    @PostConstruct
    public void init() {
        System.out.println("websocket 加载");
    }

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {
        sessionSet.add(session);
        // 在线数加1
        int cnt = ONLINE_COUNT.incrementAndGet();
        log.info("有连接加入，当前连接数为：{}", cnt);
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {
        sessionSet.remove(session);
        int cnt = ONLINE_COUNT.decrementAndGet();
        log.info("有连接关闭，当前连接数为：{}", cnt);
    }

    /**
     * 出现错误
     *
     * @param session Session
     * @param error   错误信息
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误：{}，Session ID： {}", error.getMessage(), session.getId());
        error.printStackTrace();
    }

    /**
     * 发送消息，实践表明，每次浏览器刷新，session会发生变化。
     *
     * @param session Session
     * @param order   订单信息
     */
    public static void sendMessage(Session session, Order order) {
        try {
            session.getBasicRemote().sendObject(String.format("%s (From Server，Session ID=%s)", order, session.getId()));
            log.debug("==========================\n已推送的支付订单通知：{} \n=====================================",order);
        } catch (IOException | EncodeException e) {
            log.error("推送的支付订单通知出错：{}",order);
            e.printStackTrace();
        }
    }

    /**
     * 群发消息
     *
     * @param order 订单信息
     */
    public static void broadCastInfo(Order order) {
        for (Session session : sessionSet) {
            if (session.isOpen()) {
                sendMessage(session, order);
            }
        }
    }
}