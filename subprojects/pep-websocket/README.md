pep-websocket
=============

`StompClientSpec.groovy` 为 STOMP 客户端测试代码，
测试前，需将
`src/test/groovy/com/proper/enterprise/platform/websocket/controller`
中内容复制到
`src/main/java/com/proper/enterprise/platform/websocket/controller`

之后通过 `./gradlew assemble appRunDebug` 启动服务，再去执行 `StompClientSpec` 中的 main 函数，即可看到效果
