pep-notice-server-push
======================

config  
注册配置 (token支持请求头或参数传递 请求头key:X-PEP-TOKEN)   
/notice/server/config/PUSH?access_token=###&pushChannel=###(IOS,HUAWEI,XIAOMI)    
POST  
requestBody  
IOS： {"pushPackage":"###","certificateId":"###","certPassword":"*###*"}  
Xiaomi： {"pushPackage":"###","appSecret":"###"}  
HUAWEI: {"pushPackage":"###","appId":"###","appSecret":"*###*"}  

PUT  同上  
GET  /notice/server/config/PUSH?access_token=###&pushChannel=###(IOS,HUAWEI,XIAOMI)  
DELETE  /notice/server/config/PUSH?access_token=###&pushChannel=###(IOS,HUAWEI,XIAOMI)  
==================================================================================================  

发送推送 (token支持请求头或参数传递 请求头key:X-PEP-TOKEN)  
/notice/server/send?access_token=###  
requestBody  
NoticeRequest  
targetExtMsg添加{"pushChannel":"IOS,HUAWEI,XIAOMI"}确定推送渠道  
noticeExtMsg定位同原customs 
公共key含义  
_proper_badge 角标 类型Integer  
_proper_pushtype 是否透传  
IOS特殊key:无      
小米特殊key:无      
华为特殊key:  
customsMap
push_type 消息推送类型(chat/video/other)  

Proper Enterprise Platform
