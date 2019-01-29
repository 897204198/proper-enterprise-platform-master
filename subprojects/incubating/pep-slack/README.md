pep-slack
=========

此模块旨在将 PEP 与 [Slack](https://slack.com/) 进行集成，
目的是将 [hubot](https://hubot.github.com/) 通过 [hubot-slack](https://slackapi.github.io/hubot-slack/) 再通过此模块，集成进 PEP 平台，
为 PEP 赋予与 hubot 交互的能力

hubot 可以与很多即时通讯工具进行适配，选用 Slack，是因为其对聊天机器人的态度比较友好，Slack 本身就有提供 [Slackbot](https://get.slack.help/hc/en-us/articles/202026038-An-introduction-to-Slackbot)

Slack 提供 [app](https://get.slack.help/hc/en-us/articles/360001537467-Learn-about-apps-and-the-App-Directory) 对 Slack 的功能进行扩展， 
并为每个 app 提供访问接口的 OAuth Token，可以在 [apps](https://api.slack.com/apps) 列表中，选择创建好的 app，并在其 `OAuth & Permissions` 界面找到 token。
此模块中可以使用 `pep.slack.token` 设置 Slack 提供的 OAuth Token。

`IntegrateSlackTest` 中已实现：

* 测试用例 `directMsg` 通过 [Slack Web API](https://api.slack.com/web)，
使用 `pep.slack.token`，[向指定 channel 发送 message](https://api.slack.com/methods/chat.postMessage) 

* 测试用例 `rtm` 请求 [rtm.connect](https://api.slack.com/methods/rtm.connect) 获得 websocket 服务地址，
使用 `pep.slack.token`，通过 [Real Time Messaging API](https://api.slack.com/rtm)，
向指定频道发送消息，并接收 token 所代表的用户能够接收到的所有 [Events](https://api.slack.com/events)

已基本实现 PEP 与 Slack 的集成，需要与 hubot 进行交互时，可先通过 RESTFul API 或 WebSocket 与 PEP 进行交互，再由 PEP 负责与 hubot 交互

### 相关资源

* [你不知道的 Slack Bot 新花样](https://zhuanlan.zhihu.com/p/22160598)
* [L](https://github.com/propersoft-cn/L) & [L in docker](https://github.com/propersoft-cn/docker-L)
* [图灵机器人](http://www.tuling123.com/)
