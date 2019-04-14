package com.proper.enterprise.platform.websocket;

import com.proper.enterprise.platform.core.PEPConstants;
import org.apache.catalina.realm.GenericPrincipal;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * stomp 拦截器
 * 连接握手前进行拦截
 * 从请求头STOMP_USER_HEADER中获取用户信息
 * 并将用户信息封装在请求信息中 用来点对点发送
 */
@Component
public class UserHeaderInterceptor implements ChannelInterceptor {

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (needToAddUserHeader(accessor)) {
            Optional<String> optional = Optional.ofNullable(accessor.getFirstNativeHeader(PEPConstants.STOMP_USER_HEADER));
            optional.ifPresent(name -> accessor.setUser(new GenericPrincipal(name, null, null)));
        }
        return message;
    }

    private boolean needToAddUserHeader(StompHeaderAccessor accessor) {
        return null != accessor
            && StompCommand.CONNECT.equals(accessor.getCommand())
            && accessor.containsNativeHeader(PEPConstants.STOMP_USER_HEADER);
    }

}
