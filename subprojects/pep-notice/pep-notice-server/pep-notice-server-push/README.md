pep-notice-server-push
======================
Proper Enterprise Platform

##华为推送服务端
**配置华为推送**

配置项|信息
----|----
appKey|应用中的唯一标识
appId|华为提供的唯一标识
appSecret|华为提供的应用密钥
pushPackage|应用打开包名

**发送消息**

消息类型|前提条件
-------|-------
通知栏消息|满足如下条件之一：  在桌面有快捷方式的应用  在应用中心置顶的应用  72小时内有华为支付成功行为的应用  应用处于运行状态  
透传消息|应用处于运行状态

**发送通知栏消息**

消息配置项|信息
----|----
appKey|应用中的唯一标识(用于查找配置信息)
targetTo|目标设备(为设备Token)
title|消息标题
content|消息内容
targetExtMsg|目标扩展信息(Map)  如:{"pushChannel":"HUAWEI"}
noticeExtMsg|消息扩展信息(Map)  chat类型消息：{"push_type":"chat","uri":"intent..."}  video和other类型消息:{"push_type":"video"}/{"push_type":"other"}  角标设置:{"customs":{"_proper_badge":2}}

在消息对象中装载以上配置项后, 调用[PushNoticeSender](/src/main/java/com/proper/enterprise/platform/notice/server/push/sender/PushNoticeSender.java)中的`send`方法进行推送消息。

**发送透传消息**

消息配置项|信息
----|----
appKey|应用中的唯一标识(用于查找配置信息)
targetTo|目标设备(为设备Token)
targetExtMsg|目标扩展信息(Map)  如:{"pushChannel":"HUAWEI"}
noticeExtMsg|消息扩展信息(Map)  透传自定义消息:{"customs":{"_proper_pushtype":"cmd",...}}

在消息对象中装载以上配置项后, 调用[PushNoticeSender](/src/main/java/com/proper/enterprise/platform/notice/server/push/sender/PushNoticeSender.java)中的`send`方法进行推送消息。
