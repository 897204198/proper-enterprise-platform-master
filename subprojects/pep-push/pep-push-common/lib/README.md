Third part jars
===============

Upload to Nexus

```
# com.xiaomi.pushapi:xiaomiPushSdk:2.2.16
$ mvn deploy:deploy-file -DgroupId=com.xiaomi.pushapi -DartifactId=xiaomiPushSdk -Dversion=2.2.16 -Dpackaging=jar -Dfile=/Users/alphahinex/.gradle/caches/modules-2/files-2.1/com.xiaomi.pushapi/xiaomiPushSdk/2.2.16/12f483d8d043b6ba3cb7d207e066d11b751bb3bf/xiaomiPushSdk-2.2.16.jar -Durl=http://hexin:hinex@nexus.propersoft.cn:8081/repository/maven-releases/ -DrepositoryId=nexus
```
