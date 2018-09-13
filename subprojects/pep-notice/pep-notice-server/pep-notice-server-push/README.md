pep-notice-server-push
======================



======================
# 小米推送 https://dev.mi.com/doc/?p=533

### 核心参数描述  

1 * String appSecret	构造Sender对象。appSecret是在开发者网站上注册时生成的密钥 

2 * String registrationId(String targetTo)  注册小米推送所生成的唯一标识Token

3 * String appKey   唯一的应用标识 获取小米发送类

4 * String packageName(String packageName)	设置app的包名packageName。packageName必须和开发者网站上申请的结果一致。

### Message配置参数描述

1 title(String title)	设置在通知栏展示的通知的标题，不允许全是空白字符，长度小于16，中英文均以一个计算。

2 description(String description)	设置在通知栏展示的通知的描述，不允许全是空白字符，长度小于128，中英文均以一个计算。

3 passThrough(int passThrough)	设置消息是否通过透传的方式送给app，1表示透传消息，0表示通知栏消息。

4 notifyType(Integer type)  设置通知类型 默认DEFAULT_SOUND  = 1;   // 使用默认提示音提示

5 restrictedPackageName(String packageName)	设置app的包名packageName。packageName必须和开发者网站上申请的结果一致。

6 notifyId(Integer id)	(可选)默认情况下，通知栏只显示一条推送消息。如果通知栏要显示多条推送消息，需要针对不同的消息设置不同的notify_id（相同notify_id的通知栏消息会覆盖之前的）。

7 payload(String payload)	(JSON格式,扩展的数据存在这里,方便app端获取)设置要发送的消息内容payload，不允许全是空白字符，长度小于4K，中英文均以一个计算。

### 满足上述参数生成Message 调用Sender.send(Message, registrationId, 发送失败的重试次数) 就可以发送推送 
Proper Enterprise Platform
