pep-auth-api
============

Proper Enterprise Platform

Authentication and Authorization Module API

主要定义了平台所使用的 `用户`、`角色`、`资源` 及相应服务的接口，其他模块在获得相关信息时，均需面向本模块的接口编程，以便与不同形式的实现解耦

目前只提供了一种基于 `Spring Security` 框架的实现 `pep-auth-common`

平台应用在需要验证及授权模块时，只能选择其中一种实现进行依赖，如 `pep-auth-common`