Third part jars
===============

Jars in this folder are not used for build system, the usage of them is to upload to Nexus

```
# com.ali.webpay:ali-webpay:1.0.0
$ mvn deploy:deploy-file -DgroupId=com.ali.webpay -DartifactId=ali-webpay -Dversion=1.0.0 -Dpackaging=jar -Dfile=./ali-webpay-1.0.0.jar -Durl=http://youruser:yourpwd@nexus.propersoft.cn:8081/repository/maven-releases/ -DrepositoryId=nexus
```
