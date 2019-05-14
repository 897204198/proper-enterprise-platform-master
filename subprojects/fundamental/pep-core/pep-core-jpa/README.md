pep-core-jpa
========

Proper Enterprise Platform

Core Module

为平台提供使用 JPA 的基础支持

# @ConstraintViolationMessage 注解
  - 解决数据约束先查后插问题
  - 唯一约束可将注解声明在字段上并填写name（约束名称）及message(违反约束时的提示消息，如果配置i18n则将massage转为i18n显示)
  - 完整性约束 将注解配置在关系维护表的对应字段上，简单理解为配置@JoinColumn的字段上,若多对多关系则配在joinTable字段上
