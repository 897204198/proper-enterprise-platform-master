package com.proper.enterprise.platform.slack;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "pep.slack")
public class SlackProperties {

    /**
     * Slack APP OAuth token
     * Could get token at https://api.slack.com/apps/{Slack_APP_ID}/oauth
     */
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
