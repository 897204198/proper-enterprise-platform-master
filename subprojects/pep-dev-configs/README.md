pep-dev-configs
===============

Proper Enterprise Platform 开发环境配置文件，做成模块的形式，可以被按照平台模式开发的项目所依赖，无需自行配置，且能够保持与平台版本同步

需要依赖本模块配置的构建脚本中，需通过如下方式引入：

```
buildscript {
    repositories { ... }
    dependencies {
        classpath "com.proper.enterprise.platform:pep-dev-configs:$PEP_VERSION"
    }
    dependencies {
        delete pepDevConfExtractPath
        def jars = configurations.classpath.files as List<File>
        ant.unjar src: jars.find { it.name.matches '.*pep-dev-configs.*'}, dest: pepDevConfExtractPath
    }
}
```

并在根项目构建脚本中定义如下变量：

```
ext {
    // 配置文件解压路径
    pepDevConfExtractPath = 'build/pep-dev-configs'
    // 配置文件根路径，在解压路径下
    pepDevConfRoot = "$pepDevConfExtractPath/META-INF"
}
```
