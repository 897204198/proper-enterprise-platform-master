pep-swagger
===========

Swagger 配置模块，引入后, 执行./gradlew assemble appRunDebug 启动应用环境后，浏览器输入以下网址即可浏览所修改的内容
可通过 /swagger-ui.html 浏览 API 文档，例如：

```
http://localhost:8080/pep/swagger-ui.html
```
常用注解如下：如若有具体需要请参考swagger的官方文档
- @Api 用在类上，说明该类的作用；
- @ApiModel 用在类上，表示对类进行说明，用于实体类中的参数接收说明；
- @ApiModelProperty 用于字段,表示对model属性的说明；
- @ApiOperation 用在 Controller 里的方法上，说明方法的作用，每一个接口的定义；
- @ApiParam 用于Controller中方法的参数说明；
- @ApiImplicitParams 用在方法上，为请求参数进行说明，如下：@ApiImplicitParams({@ApiImplicitParam1,@ApiImplicitParam2,...})；
- @ApiResponse 用于方法上，说明接口响应的一些信息，@ApiResponses组装多个@ApiResponse

@ApiOperation("''‍''...")、@ApiModelProperty() 等其他注解的括号内第一个为中文时，需在第一个中文前加 ''‍'' 零等宽字符，使checkStyle检查成功；
可以参考AdminFeedbackController的 @ApiOperation 注解。