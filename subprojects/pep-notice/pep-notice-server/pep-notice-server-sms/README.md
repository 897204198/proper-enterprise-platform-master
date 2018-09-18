pep-notice-sms
==============
Proper Enterprise Platform

config  
注册配置  
/notice/server/config/SMS?accessToken=###
POST  
requestBody  
{"smsUrl":"####","smsTemplate":"####","smsCharset":"####"}  
PUT  同上  
GET  /notice/server/config/SMS?accessToken=###  
DELETE  /notice/server/config/SMS?accessToken=###  
==================================================================================================  

发送邮件
/notice/server/send?accessToken=###  
requestBody  
NoticeRequest  
