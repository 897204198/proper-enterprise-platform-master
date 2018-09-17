pep-swagger
===========

Swagger 配置模块，引入后，可通过 /swagger-ui.html 浏览 API 文档，例如：

```
http://localhost:8080/pep/swagger-ui.html
```

注解@ApiOperation("''‍''...") 括号内第一个中文前需要加 ''‍'' 零等宽字符，使checkStyle检查成功；
可以参考AdminFeedbackController的 @ApiOperation 注解。