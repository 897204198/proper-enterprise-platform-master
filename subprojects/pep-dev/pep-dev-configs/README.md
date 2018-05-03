pep-dev-configs
===============

Proper Enterprise Platform 开发环境配置文件，做成模块的形式，可以被按照平台模式开发的项目所依赖，无需自行配置，且能够保持与平台版本同步

需要依赖本模块时，在根项目入口构建脚本中加入如下内容：

```
ext {
    // 配置文件解压路径
    pepDevConfExtractPath = "${rootProject.projectDir}/build/pep-dev-configs"
    // 配置文件根路径，在解压路径下
    pepDevConfRoot = "$pepDevConfExtractPath/META-INF"
}

buildscript {
    repositories { ... }
    dependencies {
        classpath "com.proper.enterprise.platform:pep-dev-configs:$PEP_VERSION"
    }
    dependencies {
        def pepDevConfExtractPath = "${rootProject.projectDir}/build/pep-dev-configs"
        delete pepDevConfExtractPath
        def jars = configurations.classpath.files as List<File>
        ant.unjar src: jars.find { it.name.matches '.*pep-dev-configs.*'}, dest: pepDevConfExtractPath
    }
}
```

> 注意更换上面代码中 `$PEP_VERSION` 为需要使用的平台配置版本

之后可以在需要引用本模块配置时，通过 `pepDevConfRoot` 路径引入，如：

```
apply from: "$pepDevConfRoot/configs/dependencies.gradle"
```
