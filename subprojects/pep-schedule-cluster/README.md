pep-schedule-cluster
====================

Proper Enterprise Platform

支持集群的计划任务模块

定义了 `pepSchedulerFactory` 工厂，统一调度计划任务。注册 trigger 时，支持按模块各自注册，之后统一合并注册进调度工厂中。

各模块注册 trigger 的方式为：定义以 `pepJobList` 开头的 List 类型 bean（如：`pepJobListAbc`），
`pepJobList` 通过 `ComposeListFactoryBean` 将所有符合 `pepJobList.+` 名称样式的 List 类型 bean 合并成为一个 List，
注入到调度工厂中。
