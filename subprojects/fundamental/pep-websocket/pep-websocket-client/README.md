pep-websocket-client
=============

使用方式
----
参照[StompClientSpec](./src/test/groovy/com/proper/enterprise/platform/websocket/client/stomp/StompClientSpec.groovy)


断线重连
----
- 提供断线重连机制 重连9次 间隔依次为15s, 15s, 30s, 180s, 1800s, 1800s, 1800s, 1800s, 3600s 若依旧未连上则放弃重连

注意事项
----
- 点对点发送 

  当需要订阅点对点发送的消息时 注意添加订阅前缀/user 
  
  如:/topic/single 后台发送send（clientId，destination，payload）
  
  前台需订阅/user/topic/single 否则无效
  
- 发送消息至后台
   
   需添加前缀/app
   
   如后台接收地址 /topic/message
   
   则客户端send地址应为/app/topic/message 否则无效 
