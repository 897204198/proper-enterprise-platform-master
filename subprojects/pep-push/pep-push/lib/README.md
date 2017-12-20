Third part jars
===============

Upload to Nexus

```
# com.ali.webpay:ali-webpay:1.0.0
$ mvn deploy:deploy-file -DgroupId=com.ali.webpay -DartifactId=ali-webpay -Dversion=1.0.0 -Dpackaging=jar -Dfile=./ali-webpay-1.0.0.jar -Durl=http://youruser:yourpwd@nexus.propersoft.cn:8081/repository/maven-releases/ -DrepositoryId=nexus
# com.cmb.b2b:cmb-b2b:1.0.0
$ mvn deploy:deploy-file -DgroupId=com.cmb.b2b -DartifactId=cmb-b2b -Dversion=1.0.0 -Dpackaging=jar -Dfile=./cmb-b2b-1.0.0.jar -Durl=http://youruser:yourpwd@nexus.propersoft.cn:8081/repository/maven-releases/ -DrepositoryId=nexus
# com.cmb.pay:cmb-pay:1.0.0
$ mvn deploy:deploy-file -DgroupId=com.cmb.pay -DartifactId=cmb-pay -Dversion=1.0.0 -Dpackaging=jar -Dfile=./cmb-pay-1.0.0.jar -Durl=http://youruser:yourpwd@nexus.propersoft.cn:8081/repository/maven-releases/ -DrepositoryId=nexus
# com.huawei.pushapi:huaweiPushSdk:0.3.12
$ mvn deploy:deploy-file -DgroupId=com.cmb.pay -DartifactId=cmb-pay -Dversion=1.0.0 -Dpackaging=jar -Dfile=./huaweiPushSdk-0.3.12.jar -Durl=http://youruser:yourpwd@nexus.propersoft.cn:8081/repository/maven-releases/ -DrepositoryId=nexus
# com.xiaomi.pushapi:xiaomiPushSdk:2.2.16
$ mvn deploy:deploy-file -DgroupId=com.xiaomi.pushapi -DartifactId=xiaomiPushSdk -Dversion=2.2.16 -Dpackaging=jar -Dfile=./xiaomiPushSdk-2.2.16.jar -Durl=http://youruser:yourpwd@nexus.propersoft.cn:8081/repository/maven-releases/ -DrepositoryId=nexus
```

