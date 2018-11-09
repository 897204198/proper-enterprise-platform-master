Third part jars
===============

Jars in this folder are not used for build system, the usage of them is to upload to Nexus

```
# com.huawei.pushapi:huaweiPushSdk:0.3.12
$ mvn deploy:deploy-file -DgroupId=com.huawei.pushapi -DartifactId=huaweiPushSdk -Dversion=0.3.12 -Dpackaging=jar -Dfile=./huaweiPushSdk-0.3.12.jar -Durl=http://youruser:yourpwd@nexus.propersoft.cn:8081/repository/maven-releases/ -DrepositoryId=nexus

# com.xiaomi.pushapi:xiaomiPushSdk:2.2.16
$ mvn deploy:deploy-file -DgroupId=com.xiaomi.pushapi -DartifactId=xiaomiPushSdk -Dversion=2.2.16 -Dpackaging=jar -Dfile=./xiaomiPushSdk-2.2.16.jar -Durl=http://youruser:yourpwd@nexus.propersoft.cn:8081/repository/maven-releases/ -DrepositoryId=nexus
```
