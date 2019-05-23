pep-core-jpa
========

Proper Enterprise Platform

Core Module

为平台提供使用 JPA 的基础支持

# 索引规约
  - 建立唯一索引强制声明索引名称，唯一索引以UK开头m，中间是表名，然后是字段名用_连接，如:UK_PEP_TEST_UNIQUE_NAME,UK_PEP_TEST_UNIQUE
  - 建议外键强制声明外键名称，名称以FK开头,依次是外键维护表名，参考表名，_ON，外键维护表字段，如:FK_ForeignTable_PrimaryTable_On_ForeignColumn 

# @ConstraintViolationMessage 注解 支持H2和MYSQL数据库
  - 解决数据约束先查后插问题
  - 唯一约束可将注解声明在字段上并填写name（约束名称）及message(违反约束时的提示消息，如果配置i18n则将massage转为i18n显示)
  - 完整性约束 将注解配置在关系维护表的对应字段上，简单理解为配置@JoinColumn的字段上,若多对多关系则配在joinTable字段上
