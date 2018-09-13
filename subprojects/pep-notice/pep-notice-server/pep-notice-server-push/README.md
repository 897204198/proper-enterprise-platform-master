pep-notice-server-push
======================

config
注册配置
/notice/server/config/PUSH?accessToken=###&pushChannel=###(IOS,HUAWEI,XIAOMI)    
POST  
requestBody  
IOS： {"pushPackage":"###","certificateId":"###","certPassword":"*###*"}  
Xiaomi： {"appSecret":"###"}  
PUT  同上  
GET  /notice/server/config/PUSH?accessToken=###&pushChannel=###(IOS,HUAWEI,XIAOMI)  
DELETE  /notice/server/config/PUSH?accessToken=###&pushChannel=###(IOS,HUAWEI,XIAOMI)  
==================================================================================================  

发送推送
/notice/server/send?accessToken=###  
requestBody  
NoticeRequest  
targetExtMsg添加{"pushChannel":"IOS,HUAWEI,XIAOMI"}确定推送渠道  
noticeExtMsg中添加原来的customs {"customs":customsMap}  
customsMap中的公共key含义  
_proper_badge 角标 类型Integer  
_proper_pushtype 是否透传  
IOS特殊key:无      
小米特殊key: 
    _proper_badge_type : notification 手机端生成通知用 固定的key:value    
    _proper_mpage : badge 设置角标用 固定的key:value
华为特殊key:      
Proper Enterprise Platform
