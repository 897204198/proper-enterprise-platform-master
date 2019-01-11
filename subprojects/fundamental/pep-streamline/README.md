pep-streamline
==============
- 在用户信息发生变更时，采用切面的方式通过streamline客户端将用户名及密码作为唯一标识与应用编码(SERVICE_KEY)进行关联

- 在移动端登录时首先访问streamline获得用户对应的SERVICE_KEY

- 以后移动端每次请求都带有SERVICE_KEY

- 利用ngx对SERVICE_KEY进行解析并指向对应的服务器

![设计图](./design/streamline.png)


Proper Enterprise Platform
