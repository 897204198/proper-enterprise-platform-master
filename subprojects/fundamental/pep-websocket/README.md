pep-websocket
=============

开发规范
----
- 订阅地址规范 
 
  1. [强制]严格遵循restful规范定义订阅地址 
  2. [强制]订阅地址统一前缀为/topic 点对点用户订阅为/user/topic
  
- 编码规范
  
  1. [建议]尽量不使用原生SimpMessagingTemplate，使用StompMessageSendUtil发送消息
  2. [强制]需要后台客户端时需使用平台提供的StompClient，避免使用其他客户端引起无限重连等问题
  3. [强制]当使用中继模式时virtualHost必须填写且不为/，避免队列消息全局污染
  4. [强制]使用平台提供的消息队列作为中继实现   
   
