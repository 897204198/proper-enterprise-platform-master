package com.proper.enterprise.platform.auth.rule;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 统一命名{模块名}+Properties.java
 * 此配置在test环境有效 若要使用迁移到 java目录下即可
 *
 * 规范
 * prefix中的{项目名}.{模块名}为配置的命名空间
 * 若需要添加配置仅需在此文件下新建成员变量及对应的get,set方法即可
 * 要求javaDoc完整，若有默认值则给成员变量添加默认值即可
 * 驼峰命名
 *
 * 满足规范且编译后 会在配置文件中查看到对于此配置的描述及默认值
 */
@Component
@ConfigurationProperties(prefix = "pep.rule")
public class RuleProperties {
}