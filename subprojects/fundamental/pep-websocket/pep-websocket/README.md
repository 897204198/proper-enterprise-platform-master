pep-websocket-server
=============

重要配置
----
- pep.websocket.broker.enable 是否开启中继代理模式 默认内存代理 中继代理模式支持集群
- pep.websocket.broker.* 消息队列配置信息 如果启用中继代理模式 必须使用消息队列对作为实现

工具类
----
- StompMessageSendUtil 支持服务端将消息推送到客户端 支持广播和点对点形式

客户端Id
----
- 如何设置客户端Id 
 
  当客户端连接至服务端时，在请求头nativeHeaders中保存信息key：PEP_STOMP_USER value:客户端id
  
  服务端会在握手前将客户端Id注册在连接信息中，根据客户端Id进行点对点发送

注意事项
----
- 如果启用中继代理模式 那么连接数跟消息队列连接数，tomcat连接数，nginx句柄数, 内存剩余均有关

中继代理模式
----
以 `RabbitMQ` 为例。

- 分隔符

    `RabbitMQ`是不支持URL "/" 分隔符, 在开启中继代理模式下需要设置分隔符为 `.`
    
    设置消息发送目的地(由于注册目标类型为 `/topic`, 设置发送目的地都以 `/topic` 开始)
    
    ```
    @SendTo("/topic/test.json")
    ```
    为了保持一致, 设置 Spring STOMP 的消息接收也以 `.`分割。
    
    - 注册`PathMatcher`为`.`。参考[Dots as Separators](https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#websocket-stomp-destination-separator)
     
    ```
    registry.setPathMatcher(new AntPathMatcher("."));
    ```
    - 设置消息接收。(由于注册了应用标识为 `/app` 见 [WebSocketMessageBrokerConfiguration](./src/main/java/com/proper/enterprise/platform/websocket/WebSocketMessageBrokerConfiguration.java), 消息接收则为`/app/test.json`)
    ```
    @MessageMapping("test.json")
    ```

- Destination

    RabbitMQ STOMP 支持了很多目标类型 [rabbitmq-Destinations](https://www.rabbitmq.com/stomp.html#d)
    
    当前仅注册了`/topic` -- SEND and SUBSCRIBE to transient and durable topics

- Virtual Hosts

    RabbitMQ 默认的配置为 `stomp.default_vhost = /`。如果指定了 `host` header, 则 RabbitMQ 将会拒绝其他连接。

