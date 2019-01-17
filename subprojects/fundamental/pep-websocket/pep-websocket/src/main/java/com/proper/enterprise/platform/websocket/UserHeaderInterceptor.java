package com.proper.enterprise.platform.websocket;

import com.proper.enterprise.platform.core.PEPConstants;
import com.proper.enterprise.platform.core.utils.CollectionUtil;
import org.apache.catalina.realm.GenericPrincipal;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;

import java.util.List;
import java.util.Optional;

@Component
public class UserHeaderInterceptor implements ChannelInterceptor {

    private static final String NATIVE_HEADERS = "nativeHeaders";

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (needToAddUserHeader(accessor, message)) {
            Optional<String> optional = extractNameFromUserHeader(message);
            optional.ifPresent(name -> accessor.setUser(new GenericPrincipal(name, null, null)));
        }
        return message;
    }

    private boolean needToAddUserHeader(StompHeaderAccessor accessor, Message<?> message) {
        return null != accessor && StompCommand.CONNECT.equals(accessor.getCommand()) && message.getHeaders().containsKey(NATIVE_HEADERS);
    }

    @SuppressWarnings("unchecked")
    protected Optional<String> extractNameFromUserHeader(Message<?> message) {
        Optional<String> optional = Optional.empty();
        LinkedMultiValueMap<String, String> nativeHeaders = (LinkedMultiValueMap<String, String>) message.getHeaders().get("nativeHeaders");
        if (nativeHeaders != null && nativeHeaders.containsKey(PEPConstants.STOMP_USER_HEADER)) {
            List<String> list = nativeHeaders.get(PEPConstants.STOMP_USER_HEADER);
            if (CollectionUtil.isNotEmpty(list)) {
                optional = Optional.of(list.get(0));
            }
        }
        return optional;
    }

}
