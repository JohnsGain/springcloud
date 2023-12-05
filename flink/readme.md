# flink 学习

## connected join union 这几种可以合并流 的算子的区别

* connected : 可以合并不同类型的两个流，保留他们原本的类型

```java
public class Demo {
    public void test() {
        DataStream<Integer> someStream = null;
        DataStream<String> otherStream=null;

        ConnectedStreams<Integer, String> connectedStreams=someStream.connect(otherStream);
    }
}
```

* join 合并两个流，需要满足以各自某个字段的值相等 作为 合并条件
* union 可以对2个以及2个以上流进行合并

## **stateful-stream-processing**

### 有状态流处理的几种场景：

* 当需要按某个事件模型搜索对应的事件时，需要按事件发生顺序记住到目前为止流过的事件
* 需要按每min/h/day 聚合事件时，需要保存某个时间段待处理的事件
* 当使用一个事件流来训练机器学习模型时，需要用状态记住当前版本的模型参数
* 当需要管理历史数据，状态可以高效地访问到历史数据

### Flink’s state backends.

> flink 有不同的状态后端， 每种状态后端有针对状态的存储方式和存储位置有不同的实现
>

### Keyed State

> 内置的k-v 存储容器，仅适用于 keyed stream
>

### State Persistence

> flink 借助于 stream replay and checkpointing 这两个特点实现了 容错的能力。checkpointing 就是需要不断抓取
> 事件流和 算子状态 的快照
>

### Barriers

* A core element in Flink’s distributed snapshotting are the stream barriers
* 是注入到事件流里面，作为事件流的一部分， 跟着事件流一起流动。
* stream barrier分离的不同的snapshot 块，超过stream barrier的进入下一个snapshot

### State and Fault Tolerance in Batch Programs

> flink把批处理作为特殊的流处理， 流处理的很多特点也适用于批处理，但是也有少量不同点
> * 由于批处理数据是有限的，批处理容错回复是从数据的最开始重新执行。这就导致 一旦批处理发生容错，
    > 会花费大量代价去执行事件重放，但是同样的，他会极大地增加正常执行情况下性能-因为没有了checkpoint
> * 批处理中状态后端 使用简单的 内存数据结构，不是 key/value indexes.
>

### Savepoints

Savepoints 是 人工触发的checkpoint

