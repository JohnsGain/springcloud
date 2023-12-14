# 1.flink 学习

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

## 2. **stateful-stream-processing**

### 2.1 有状态流处理的几种场景：

* 当需要按某个事件模型搜索对应的事件时，需要按事件发生顺序记住到目前为止流过的事件
* 需要按每min/h/day 聚合事件时，需要保存某个时间段待处理的事件
* 当使用一个事件流来训练机器学习模型时，需要用状态记住当前版本的模型参数
* 当需要管理历史数据，状态可以高效地访问到历史数据

### 2.2 Flink’s state backends.

> flink 有不同的状态后端， 每种状态后端有针对状态的存储方式和存储位置有不同的实现
>

### 2.3 Keyed State

> 内置的k-v 存储容器，仅适用于 keyed stream
>

### 2.4 State Persistence

> flink 借助于 stream replay and checkpointing 这两个特点实现了 容错的能力。checkpointing 就是需要不断抓取
> 事件流和 算子状态 的快照
>

#### 2.4.1 Unaligned Checkpointing

* 不对齐checkpoint 更接近 Chandy-Lamport algorithm。
* 不对齐checkpoint可以让每个stream barrier尽快到达sink算子
* 适用于那种对齐checkpoint需要等待大量时间的场景

#### 2.4.2 Unaligned Recovery

Operators first recover the in-flight data before starting processing any data from upstream operators in unaligned
checkpointing. Aside from that, it performs the same steps as during recovery of aligned checkpoints.

#### 2.4.3 Exactly Once vs. At Least Once

* flink有一个开关可以禁用在做checkpoint的时候的 对齐操作， 对那些需要对整个事件流保持
  低延迟(几毫秒内)的场景特别有用

### 2.5 Barriers

* A core element in Flink’s distributed snapshotting are the stream barriers
* 是注入到事件流里面，作为事件流的一部分， 跟着事件流一起流动。
* stream barrier分离的不同的snapshot 块，超过stream barrier的进入下一个snapshot

### 2.6 State and Fault Tolerance in Batch Programs

> flink把批处理作为特殊的流处理， 流处理的很多特点也适用于批处理，但是也有少量不同点
> * 由于批处理数据是有限的，批处理容错回复是从数据的最开始重新执行。这就导致 一旦批处理发生容错，
    > 会花费大量代价去执行事件重放，但是同样的，他会极大地增加正常执行情况下性能-因为没有了checkpoint
> * 批处理中状态后端 使用简单的 内存数据结构，不是 key/value indexes.
>

### 2.7 Savepoints

* Savepoints 是 人工触发的checkpoint
* Note that savepoints will always be aligned.
* Savepoints are similar to checkpoints except that they are triggered by the user and
  don’t automatically expire when newer checkpoints are completed.
* All programs that use checkpointing can resume execution from a savepoint. Savepoints allow both updating your
  programs and your Flink cluster without losing any state.

### 2.8 align 流对齐

> * 从多个输入流接受事件的算子，当从其中某个输入流接受到stream barrier之后，不会再处理来自这个
    > 输入流的更多事件。直到所有的输入流的stream barrier都到达算子之后，才会处理新进来
> * 当最后一个输入流的stream barrier到达 算子，就会把挂起待处理的所有记录发送到下游
> * 它对状态进行快照，并从所有输入流中恢复处理记录，在处理来自流的记录之前处理来自输入缓冲区的记录。
> * 最后，算子会把状态保存到 状态后端
>

### 2.9 Snapshotting Operator State

> When operators contain any form of state, this state must be part of the snapshots as well.

#### 2.9.1 The resulting snapshot now contains:

* For each parallel stream data source, the offset/position in the stream when the snapshot was started
* For each operator, a pointer to the state that was stored as part of the snapshot

#### 2.9.2 recovery

> 发生故障(硬件，网络，程序),flink会选取最新一次成功的checkpoint k,程序会重新部署整个分布式数据处理流，
> 每个算子会恢复 到和与checkpoint k一起保存的算子状态,source算子重新从位置Sk 读取事件流。例如使用
> kafka数据源，意味着将从偏移量Sk处理拉取消息消费。
> Upon a failure, Flink selects the latest completed checkpoint k. The system then re-deploys the entire distributed
> dataflow, and gives each operator the state that was snapshotted as part of checkpoint k. The sources are set to start
> reading the stream from position Sk. For example in Apache Kafka, that means telling the consumer to start fetching from
> offset Sk.
> If state was snapshotted incrementally, the operators start with the state of the latest full snapshot and then apply
> a series of incremental snapshot updates to that state.

## 3.Streaming Analytics

### Event Time

flink提供3个时间概念

* event time: the time when an event occurred, as recorded by the device producing (or storing) the event

* ingestion time: a timestamp recorded by Flink at the moment it ingests the event

* processing time: the time when a specific operator in your pipeline is processing the event

### Watermarks

* 水印是一个时间戳，Flink可以给数据流添加水印来解决数据延迟的问题。
* 水印并不会影响原有的EventTime时间
* 当数据流添加水印后，会按照水印时间来触发窗口计算。
* 一般设置水印时间，比事件时间小几秒钟，表示最大允许数据延迟达到多久。
* 水印时间 = 事件时间 - 允许延迟时间
* 当接收到的 水印时间 >= 窗口结束时间，则触发计算 如等到一条数据的水印时间为10:10:00 >= 10:10:00
  才触发计算,也就是要等到事件时间为10:10:03的数据到来才触发计算：(即事件时间 - 允许延迟时间 >= 窗口结束时间 或
  事件时间 >= 窗口结束时间 + 允许延迟时间)

### window

* tumbling window: 基于事件和时间两种
  tumbing time window ; tumbling count window

> 时间对齐，窗口长度固定，没有重叠。

* sliding window： 基于事件和时间两种

> 时间不是对齐，窗口长度固定，会有重叠计算
> sliding time win; sliding count win

* session window

> 一个session窗口通过一个session间隔来配置，这个session间隔定义了非活跃周期的长度，当这个非活跃周期产生，那么当前的session将关闭并且后续的元素将被分配到新的session窗口中去

* global window

### 使用 processing time 会有一些不足

> 不足
> * can not correctly process historic data,
> * can not correctly handle out-of-order data,
> * results will be non-deterministic,

> 优势
> * f lower latency.

### Late Events 迟到事件的两种处理方式

#### 默认情况下，迟到事件会被丢弃，除此之外，flink提供了两种额外方式来处理迟到事件

* sideOutputLateData

 ```java
 public class Demo {
    public void test() {
        OutputTag<Event> lateTag = new OutputTag<Event>("late") {
        };

        SingleOutputStreamOperator<Event> result = stream
                .keyBy(...)
     .window(...)
     .sideOutputLateData(lateTag)
                .process(...);

        DataStream<Event> lateStream = result.getSideOutput(lateTag);
    }
}
 ```

* allowedLateness

```java
public class Demo {
    public void test() {
        stream
                .keyBy(...)
                .window(...)
                .allowedLateness(Time.seconds(10))
                .process(...);
    }
}
```

### Surprises  窗口函数可能出现的意想不到的情况

> Some aspects of Flink’s windowing API may not behave in the way you would expect. Based on frequently asked questions
> on the flink-user mailing list and elsewhere, here are some facts about windows that may surprise you.

* 滑动窗口的事件 可能被复制到多个窗口
* 基于时间的窗口， 窗口时间范围是按照 操作系统整点时间来划分窗口的，比如一个 1小时划分一个窗口
  的程序，程序在12.05分启动，它的第一个窗口是在13点结束，而不是13.05分
* Windows Can Follow Windows， window函数后面可以跟着 windowAll
* 没有事件生成的窗口
* 延迟事件可能产生合并