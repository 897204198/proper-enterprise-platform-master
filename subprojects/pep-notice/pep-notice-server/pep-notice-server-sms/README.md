pep-notice-sms
==============
Proper Enterprise Platform

config  
注册配置(token支持请求头或参数传递 请求头key:X-PEP-TOKEN)    
/rest/notice/server/config/SMS?access_token=###
POST  
requestBody  
{"smsUrl":"####","smsTemplate":"####","smsCharset":"####"}  
PUT  同上  
GET  /rest/notice/server/config/SMS?access_token=###  
DELETE  /notice/server/config/SMS?access_token=###  
==================================================================================================  

发送邮件 (token支持请求头或参数传递 请求头key:X-PEP-TOKEN)  
/rest/notice/server/send?access_token=###  
requestBody  
NoticeRequest  
