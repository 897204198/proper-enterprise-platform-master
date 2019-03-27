pep-websocket
=============

开发规范
-------

- 路径规范
 
  1. [强制] 消息映射统一前缀为 `/app`
  1. [强制] 订阅路径统一前缀为 `/topic` 点对点用户订阅为 `/user/topic`
  1. [强制] 为兼容消息中间件，路径间隔符使用 `.`，如 `/app/foo.bar`，`/topic/foo.bar` 等
  1. [强制] `@MessageMapping` 中设定的路径不包含消息映射统一前缀，且不带根，如 `@MessageMapping("foo.bar")`
  1. [强制] 客户端发送消息时需要前缀，如 `/app/foo.bar`
  
- 编码规范
  
  1. [建议] 尽量不使用原生SimpMessagingTemplate，使用StompMessageSendUtil发送消息
  1. [建议] 需要后台客户端时需使用平台提供的StompClient，避免使用其他客户端引起无限重连等问题
  1. [建议] 当使用中继模式时 `pep.websocket.broker.virtual-host` 必须填写且不为 `/`，避免队列消息全局污染
  1. [建议] 使用平台提供的消息队列作为中继实现
  1. [建议] 为减小资源浪费，面向一个地址的长连接，尽量只创建一个，所有相关消息都可以在一个长连接上发送及接收，务必避免类似 HTTP 方式一样，一个消息创建一个长连接！


------------


pep-websocket-server
====================

重要配置
-------

- pep.websocket.broker.enable 是否开启中继代理模式 默认内存代理 中继代理模式支持集群
- pep.websocket.broker.* 消息队列配置信息 如果启用中继代理模式 必须使用消息队列对作为实现

工具类
----
- StompMessageSendUtil 支持服务端将消息推送到客户端 支持广播和点对点形式

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


---------


pep-websocket-client
====================

使用方式
----
参照[StompClientSpec](./pep-websocket-client/src/test/groovy/com/proper/enterprise/platform/websocket/client/stomp/StompClientSpec.groovy)


断线重连
----
- 提供断线重连机制 重连9次 间隔依次为15s, 15s, 30s, 180s, 1800s, 1800s, 1800s, 1800s, 3600s 若依旧未连上则放弃重连

注意事项
----
- 点对点发送 

  当需要订阅点对点发送的消息时 注意添加订阅前缀/user 
  
  如:/topic/single 后台发送send（clientId，destination，payload）
  
  前台需订阅/user/topic/single 否则无效
  
- 客户端如何设置用户Id 
 
  当客户端连接至服务端时，在请求头nativeHeaders中保存信息key：PEP_STOMP_USER value:用户id
  
  服务端会在握手前将用户Id注册在连接信息中，根据用户Id进行点对点发送
  
- 发送消息至后台
   
   需添加前缀/app
   
   如后台接收地址 `foo.bar`
   
   则客户端send地址应为 `/app/foo.bar` 否则无效
