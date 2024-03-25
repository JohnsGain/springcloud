### the execution mode can be configured via the execution.runtime-mode setting. There are three possible values:

* STREAMING: The classic DataStream execution mode (default)
* BATCH: Batch-style execution on the DataStream API
* AUTOMATIC: Let the system decide based on the boundedness of the sources

设置flink 执行模式的2种方式：

bin/flink run -Dexecution.runtime-mode=BATCH <jarFile>

```java
public class dd {
    public void dd() {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setRuntimeMode(RuntimeExecutionMode.BATCH);
    }
}

```

batchMode 和 StreamingMode的区别
1.批处理适用于事件数量在任务执行之前就已知的场景，
流模式适用于事件数量未知，需要一直不停在执行，持续不断的在线实时处理

2. 批处理可以进行更多的数据结构和算法方面的优化，批处理取消了stateBackend和 checkpoint
   可以更高效的处理

3.流处理需要实时更新和流水线作业，算子消息处理完立刻流到下游算子。 批处理可以在上一个算子
完全处理完之后，把上游算子的处理中间结果持久化保存， 然后统一流到下游。

4.在STREAMING模式下，用户定义函数不应该对传入记录的顺序做出任何假设。数据一到达就被处理。
在BATCH执行模式下，Flink保证了一些操作的顺序。
Functions, or Operators, that consume multiple input types will process them in the following order:

broadcast inputs are processed first
regular inputs are processed second
keyed inputs are processed last

For functions that consume from multiple regular or broadcast inputs — such as a CoProcessFunction — Flink has the right
to process data from any input of that type in any order.

For functions that consume from multiple keyed inputs — such as a KeyedCoProcessFunction — Flink processes all records
for a single key from all keyed inputs before moving on to the next.

4. Failure Recovery #
