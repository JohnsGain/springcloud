# Timely Stream Processing

[参考](https://nightlies.apache.org/flink/flink-docs-master/docs/concepts/time/)

## 1. Event Time and Watermarks

* 水印用来测量在一个事件流里面 事件时间的进展程度
* 一个水印时间t 表示 在那个流处理里面，事件时间已经达到了时间t，meaning that there should be no more elements from the
  stream with a timestamp t’ <= t
* 在事件时间有序的流处理里面，水印时间只是一个简单的周期性标记，但是对于乱序的流处理里面
  ，水印时间就至关重要。

### 1.1 Watermarks in Parallel Streams

> Some operators consume multiple input streams;union, for example, or operators following a keyBy(…) or partition(…)
> function. Such an operator’s current event time is the minimum of its input streams’ event times. As its input streams
> update their event times, so does the operator.
> 一些算子会接收多个输入流，例如 union,或者一些跟在 keyBy,partition等函数后面的算子，这些算子的当前事件时间是他接收的所有输入事件中的
> 最小事件时间。
> 一旦这些输入事件的事件时间开始更新，算子的事件时间也会跟着更新

## 2. Lateness

基于包括如下面这样的原因：

* 真实的应用场景，事件延迟事件是随机不定的，期望在某个水印时间到达的时候，小于这个水印时间的
  事件不再出现， 这样的期望几乎不可能达到。
* 即使做了允许固定时间范围的延迟， 但是有时候也会出现延迟事件太多，导致计算结果不满足应用需要，

需要专门对延迟事件做处理，

* sideOutputLateData
* allowedLateness

