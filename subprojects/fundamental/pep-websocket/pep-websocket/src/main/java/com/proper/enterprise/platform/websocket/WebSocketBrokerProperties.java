package com.proper.enterprise.platform.websocket;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "pep.websocket.broker")
public class WebSocketBrokerProperties {

    /**
     * Use external broker or not
     */
    private boolean enable;

    /**
     * Host of relay broker
     */
    private String relayHost = "localhost";

    /**
     * Port of relay broker
     */
    private int relayPort = 61613;

    /**
     * External broker user
     */
    private String user = "guest";

    /**
     * External broker password
     */
    private String password = "guest";

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getRelayHost() {
        return relayHost;
    }

    public void setRelayHost(String relayHost) {
        this.relayHost = relayHost;
    }

    public int getRelayPort() {
        return relayPort;
    }

    public void setRelayPort(int relayPort) {
        this.relayPort = relayPort;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
