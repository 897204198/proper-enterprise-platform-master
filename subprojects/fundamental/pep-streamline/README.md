pep-streamline
==============
- Streamline的作用

  * 由于`WebView`的引入, 减少对移动端的依赖, 在多个项目引申出同一个`APP`能够根据不同的用户展示不同的内容。
  * `streamline` 则提供了相应的服务, 使用`SERVICE-KEY`这个关键标识, 对不同的用户进行区分, 实现一个`APP`能够阅览相应的内容。

- SERVICE-KEY是什么

  `SERVICE-KEY`是项目的唯一标识, 一般为我们在项目创建的时候对项目的简化命名，如：`propersoft`, `ihos`, `icmp`等
  
- SERVICE-KEY与用户的关系是如何建立的

  目前streamline提供两种方式来区分服务
  
  1. 通过用户的用户名和密码作为签名，签名与`SERVICE-KEY`有严格的一对一关联
    
        - 此方法需要后台应用依赖streamline的客户端
        
        - 在用户信息发生变更时, 客户端采用切面的方式通过将用户名及密码或签名作为唯一标识与`SERVICE-KEY`发送到`streamline`服务端进行信息更新。
        
        - 用户删除时, 客户端采用切面的方式通过将用户名及密码或签名作为唯一标识与`SERVICE-KEY`发送到`streamline`服务端进行注销。
        
        - 历史数据处理
             ```sql
             -- 普日项目的注册streamline服务的脚本, propersoft 为普日项目的 SERVICE-KEY, 使用用户名和密码作为签名 --
             INSERT IGNORE INTO PEP_STREAMLINE_SIGN (ID, BUSINESS_ID, SIGNATURE, SERVICE_KEY)
             SELECT UUID() AS ID, pau.id as BUSINESS_ID, MD5(CONCAT(pau.USERNAME,'$#',pau.`password`)) as SIGNATURE, 'propersoft' as SERVICE_KEY
               from pep_auth_user pau
             WHERE pau.id <> 'pep-sysadmin';
             ```
  2. 直接在服务端添加签名和`SERVICE-KEY`的对应关系
      
      - 此方法安全性更强，相当于一个服务对应一个签名，签名可以在移动端理   
  
  
- 移动端是如何获取到SERVICE-KEY的

  移动端在登录前会向`streamline`服务端发送请求可以直接以签名为参数或者以用户名密码为参数获取到`SERVICE-KEY`
  
- 移动端如何通过`SERVICE-KEY`找到对应前台

  根据约定从**/`SERVICE-KEY`/获取前端对应地址,约定前台地址均以通用的域名开始, 如: `https://icmp.propersoft.cn/`, 拼接相应的 `SERVICE-KEY` 获取前端页面, 普日的前端页面为 `https://icmp.propersoft.cn/propersoft`

- 移动端如何通过SERVICE-KEY找到对应后台

  > 移动端也有直接请求后台接口, 如登录接口。
  1. ngx 提供一个路由, 如 `https://icmp.propersoft.cn/**/route`为后台接口的根`URL`。
  2. 移动端每次请求后台需要在请求头中加上 `X-SERVICE-KEY: SERVICE-KEY`。
  3. ngx 获取请求头中的 `SERVICE-KEY` 后, 进行相应的转发至后台服务器。
  
![设计图](./design/streamline.png)


Proper Enterprise Platform
