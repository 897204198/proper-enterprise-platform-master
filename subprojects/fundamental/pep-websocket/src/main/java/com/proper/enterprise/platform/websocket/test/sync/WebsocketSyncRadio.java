package com.proper.enterprise.platform.websocket.test.sync;

import com.proper.enterprise.platform.core.utils.DateUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.websocket.test.HoldUserHeaderInterceptor;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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
        String user = randomUser();
        template.convertAndSendToUser(user, "/topic/sync", "syncSendOne: " + user + " " + DateUtil.getTimestamp());
    }

    private String randomUser() {
        return HoldUserHeaderInterceptor.getUserHolder().get(RandomUtils.nextInt(0, HoldUserHeaderInterceptor.getUserHolder().size()));
    }

    @Scheduled(cron = "0/2 * * * * ?")
    public void randomSendSome() {
        String[] users = new String[]{randomUser(), randomUser(), randomUser()};
        for (String user : users) {
            if (StringUtil.isBlank(user)) {
                continue;
            }
            template.convertAndSendToUser(user, "/topic/sync",
                "syncSendSome: " + StringUtil.join(users, ",") + " " + DateUtil.getTimestamp());
        }
    }

}
