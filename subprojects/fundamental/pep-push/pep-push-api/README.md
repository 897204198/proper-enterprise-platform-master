pep-push-api
============

Proper Enterprise Platform

主要对应推送平台相关接口，包括应用服务器向推送平台推送消息及手机端帐号绑定，取消帐号绑定等接口。

## 移动端推送的概念和原理
概念：服务端向手机端实时地下发消息。
原理：移动端设备需要与服务端保持一条tcp长连接，在连接断开后，要有重连机制。服务端通过这条TCP长连接，实现向手机端下发消息。

对于这条TCP长连接，可以是手机端系统级别的，由手机端操作系统来建立和维持这条TCP长连接，比如IOS使用apns,Android使用GCM，这时所有的手机端程序可以共用这一条TCP连接，这样可以减少流量和电量消耗，也可以提高推送的稳定性。

也可以是应用级别的，这样，我们自己搭建推送服务端，由我们自己的手机端程序与来维持一条推送的TCP长连接。这样的好处是我们自己可以处理推送的各种细节，可以扩展基础的推送功能。缺点是，增加了手机端的流量和电量的消耗的同时，推送也也能是不稳定的。特别是在IOS平台上，应用程序不能长时间后台运行。在一些android机型上，这种问题也越来越常见，比如小米和华为的手机，应用的后台运行和开机自启功能做了限制。

## 推送框架的实现
- ios端的推送
 直接集成苹果官方推送apns，使用Apple的最新的基于HTTP/2 APNS协议，使用了开源库[pushy](https://github.com/relayrides/pushy)
- android端推送
 可以支持多个平台的推送，客户端集成多种推送方式，根据手机端的类型，动态绑定一种推送方式。
 现在支持小米推送及华为推送，实现的逻辑时在华为手机上，启用华为推送；在其它手机上，使用小米推送。主要原因是小米推送的客户端兼容性更好，华为推送在某些非华为手机上有推送不成功的情况。
 在小米手机及华为手机上分别都是系统级别的长连接，消息到达率更高，其它类型手机消息到达率可能低一些。
  
## 相关资料参考
- [华为推送官网](http://developer.huawei.com/push)
- [小米推送官网](http://dev.xiaomi.com/console/appservice/push.html)
- [Android推送技术研究](http://blog.csdn.net/softwave/article/details/50997545)
- [Android端外推送到底有多烦？](http://ju.outofmemory.cn/entry/273993)
- [Android push方案调研](http://www.in-droid.com/2016/03/25/Android-push方案调研/)
- [Android 第三方 Push 推送方案使用调查](https://github.com/android-cn/topics/issues/4)