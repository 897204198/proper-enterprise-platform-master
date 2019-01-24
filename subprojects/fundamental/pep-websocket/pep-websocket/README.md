pep-websocket
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


