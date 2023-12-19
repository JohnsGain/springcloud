# Event-driven Applications

[参考](https://nightlies.apache.org/flink/flink-docs-master/docs/learn-flink/event_driven/)

## 1. Introduction

事件驱动应用 ProcessFunction 结合了 状态流 和 及时流 处理的特点 ，和 RichFlatMapFunction
有点相似，但是多了 时间流处理的特性

ProcessFunctions 类型的接口有多个，如 KeyedProcessFunction， CoProcessFunctions，BroadcastProcessFunctions, etc


