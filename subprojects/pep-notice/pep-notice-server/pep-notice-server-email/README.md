pep-notice-email
================
Proper Enterprise Platform

config  
注册配置  
/notice/server/config/EMAIL?accessToken=###
POST  
requestBody  
{"mailServerHost":"####","mailServerPort":"####","mailServerUsername":"####","mailServerPassword":"####","mailServerUseSSL":true/false,"mailServerUseTLS":true/false,"mailServerDefaultFrom":"####"}  
腾讯邮箱服务 mailServerUseSSL 为 true, mailServerUseTLS 为 false  
PUT  同上  
GET  /notice/server/config/EMAIL?accessToken=###  
DELETE  /notice/server/config/EMAIL?accessToken=###  
==================================================================================================  

发送邮件
/notice/server/send?accessToken=###  
requestBody  
NoticeRequest  
targetExtMsg添加可选参数{"cc":"###","bcc":"###","replyTo":"###"}  
noticeExtMsg添加可选参数{"from":"###","sentDate":"2017-12-29T10:23:23.998Z""}  
