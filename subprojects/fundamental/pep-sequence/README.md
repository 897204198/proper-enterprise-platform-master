pep-sequence
============
Proper Enterprise Platform

流水号生成规则配置
----
配置流水号生成规则信息

配置项|配置描述
---|---
Code|唯一约束, 用于其他功能访问。必填
名称|用于其他功能访问时显示。必填
公式|流水号公式: HT${date:时间}${length:流水} 示例：HT${date:yyyyMM}${length:3} HT201808003
清零方式|数据字典: 不清零、日清零、月清零、年清零

在需要生成流水号的数据插入之前调用 [SerialNumberUtil](./src/main/java/com/proper/enterprise/platform/sequence/util/SerialNumberUtil.java) 中的 `generate(sequenceCode)` 方法获取流水号。
