pep-cache-ehcache
=================

EhCache 实现 `CacheDuration` 中的 `ttl` 和 `maxIdleTime` 支持的最小单位是 `秒`，毫秒级别的配置会导致 EhCache 的 `timeToLiveSeconds` 和 `timeToIdleSeconds` 均为 **0**。
