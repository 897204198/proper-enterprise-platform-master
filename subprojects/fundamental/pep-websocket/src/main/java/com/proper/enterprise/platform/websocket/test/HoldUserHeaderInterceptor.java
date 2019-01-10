package com.proper.enterprise.platform.websocket.test;

import com.proper.enterprise.platform.websocket.UserHeaderInterceptor;
import org.springframework.context.annotation.Primary;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@Primary
public class HoldUserHeaderInterceptor extends UserHeaderInterceptor {

    private static final List<String> USER_HOLDER = new ArrayList<>();

    public static List<String> getUserHolder() {
        return USER_HOLDER;
    }

    @Override
    protected Optional<String> extractNameFromUserHeader(Message<?> message) {
        Optional<String> optional = super.extractNameFromUserHeader(message);
        optional.ifPresent(USER_HOLDER::add);
        return optional;
    }

}
