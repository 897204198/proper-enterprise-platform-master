package com.proper.enterprise.platform.websocket.util;

import com.proper.enterprise.platform.core.PEPApplicationContext;
import org.springframework.messaging.simp.SimpMessagingTemplate;

/**
 * stomp消息发送工具类
 */
public class StompMessageSendUtil {

    private StompMessageSendUtil() {
    }

    /**
     * 向客户端广播消息
     *
     * @param destination 广播地址
     * @param payload     消息内容
     */
    public static void send(String destination, Object payload) {
        PEPApplicationContext.getBean(SimpMessagingTemplate.class).convertAndSend(destination, payload);
    }


    /**
     * 向指定客户端发送消息
     *
     * @param clientId    接收消息的客户端id
     * @param destination 广播地址
     * @param payload     消息内容
     */
    public static void send(String clientId, String destination, Object payload) {
        PEPApplicationContext.getBean(SimpMessagingTemplate.class).convertAndSendToUser(clientId, destination, payload);
    }
}
