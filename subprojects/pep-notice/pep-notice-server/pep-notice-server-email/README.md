pep-notice-email
================
Proper Enterprise Platform

config  
注册配置(token支持请求头或参数传递 请求头key:ACCESS_TOKEN)  
/rest/notice/server/config/EMAIL?access_token=###
POST  
requestBody  
{"mailServerHost":"####","mailServerPort":"####","mailServerUsername":"####","mailServerPassword":"####","mailServerUseSSL":true/false,"mailServerUseTLS":true/false,"mailServerDefaultFrom":"####"}  
腾讯邮箱服务 mailServerUseSSL 为 true, mailServerUseTLS 为 false  
PUT  同上  
GET  /rest/notice/server/config/EMAIL?access_token=###  
DELETE  /rest/notice/server/config/EMAIL?access_token=###  
==================================================================================================  

发送邮件
(token支持请求头或参数传递 请求头key:X-PEP-TOKEN)  
/rest/notice/server/send?access_token=###  
requestBody  
NoticeRequest  
targetExtMsg添加可选参数{"cc":"###","bcc":"###","replyTo":"###"}  
noticeExtMsg添加可选参数{"from":"###","sentDate":"2017-12-29T10:23:23.998Z""}  
