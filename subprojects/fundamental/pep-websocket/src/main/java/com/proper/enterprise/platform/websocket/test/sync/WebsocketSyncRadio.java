package com.proper.enterprise.platform.websocket.test.sync;

import com.proper.enterprise.platform.core.utils.CollectionUtil;
import com.proper.enterprise.platform.core.utils.DateUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.websocket.test.HoldUserHeaderInterceptor;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component("websocketSyncRadio")
public class WebsocketSyncRadio {

    @Autowired
    private SimpMessagingTemplate template;

    @Scheduled(cron = "0/15 * * * * ?")
    public void pushAll() {
        template.convertAndSend("/topic/sync", "syncSendAll:" + DateUtil.getTimestamp());
    }

    @Scheduled(cron = "* * * * * ?")
    public void randomSendOne() {
        randomUser().ifPresent(user ->
            template.convertAndSendToUser(user, "/topic/sync", "syncSendOne: " + user + " " + DateUtil.getTimestamp())
        );
    }

    private Optional<String> randomUser() {
        List<String> userHolder = HoldUserHeaderInterceptor.getUserHolder();
        if (CollectionUtil.isNotEmpty(userHolder)) {
            return Optional.of(userHolder.get(RandomUtils.nextInt(0, userHolder.size())));
        } else {
            return Optional.empty();
        }
    }

    @Scheduled(cron = "0/2 * * * * ?")
    public void randomSendSome() {
        int count = 3;
        Set<String> userSet = new HashSet<>(count);
        for (int i = 0; i < count; i++) {
            randomUser().ifPresent(userSet::add);
        }
        for (String user : userSet) {
            template.convertAndSendToUser(user, "/topic/sync",
                "syncSendSome: " + StringUtil.join(userSet.toArray(), ",") + " " + DateUtil.getTimestamp());
        }
    }

}
