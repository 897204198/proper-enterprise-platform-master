package com.proper.enterprise.platform.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@PropertySource(value = "classpath:application-websocket.properties")
public class WebSocketMessageBrokerConfiguration implements WebSocketMessageBrokerConfigurer {

    private static final String[] ENDPOINTS = new String[]{"/stomp"};
    private static final String[] APP_DES_PREFIXES = new String[]{"/app"};
    private static final String[] DES_PREFIXES = new String[]{"/topic"};

    /**
     * 当没有用户和服务可以处理消息的时候 统一向该地址发送广播
     */
    private static final String USER_DEST_BROADCAST = "/topic/pep-unresolved-user";

    /**
     * 集群场景下的用户注册地址  使多服务端间用户透明
     */
    private static final String USER_REGISTRY_BROADCAST = "/topic/pep-user-registry";

    @Value("${pep.access-control.allow-origin:*}")
    private String allowedOrigins;
    @Value("${pep.websocket.broker.enable}")
    private boolean useExternalBroker;
    @Value("${pep.websocket.broker.relay-host}")
    private String relayHost;
    @Value("${pep.websocket.broker.relay-port}")
    private int relayPort;
    @Value("${pep.websocket.broker.user}")
    private String brokerUser;
    @Value("${pep.websocket.broker.password}")
    private String brokerPwd;
    @Value("${pep.websocket.broker.virtual-host}")
    private String virtualHost;

    private UserHeaderInterceptor userHeaderInterceptor;

    @Autowired
    public WebSocketMessageBrokerConfiguration(UserHeaderInterceptor userHeaderInterceptor) {
        this.userHeaderInterceptor = userHeaderInterceptor;
    }

    /**
     * 注册stomp
     * 默认允许跨域访问 以socket形式启动
     *
     * @param registry 注册器
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(ENDPOINTS).setAllowedOrigins(allowedOrigins).withSockJS();
    }

    /**
     * 配置消息代理
     * 默认简单代理
     * 当设置useExternalBroker为true后启动中继代理
     * 中继代理需要消息队列实现
     *
     * @param registry 注册器
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setPathMatcher(new AntPathMatcher("."));
        registry.setApplicationDestinationPrefixes(APP_DES_PREFIXES);
        if (useExternalBroker) {
            registry.enableStompBrokerRelay(DES_PREFIXES)
                .setUserDestinationBroadcast(USER_DEST_BROADCAST)
                .setUserRegistryBroadcast(USER_REGISTRY_BROADCAST)
                .setRelayHost(relayHost).setRelayPort(relayPort)
                .setSystemLogin(brokerUser).setSystemPasscode(brokerPwd)
                .setClientLogin(brokerUser).setClientPasscode(brokerPwd)
                .setVirtualHost(virtualHost);
        } else {
            registry.enableSimpleBroker(DES_PREFIXES);
        }
    }

    /**
     * 配置握手前拦截器
     * 注册用户信息拦截器
     *
     * @param registration 注册器
     */
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(userHeaderInterceptor);
    }

}
