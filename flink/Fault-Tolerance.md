# Fault-Tolerance

[参考](https://nightlies.apache.org/flink/flink-docs-master/docs/learn-flink/fault_tolerance/)

### 2. Exactly Once End-to-end #

To achieve exactly once end-to-end, so that every event from the sources affects the sinks exactly once, the following
must be true:

* your sources must be replayable, and
* your sinks must be transactional (or idempotent)

### 3. Checkpoint Storage

The location where these snapshots are stored is defined via the jobs checkpoint storage. Two implementations of
checkpoint storage are available

* FileSystemCheckpointStorage ： Distributed file system
  * Supports very large state size
  * Highly durable
  * Recommended for production deployments
* JobManagerCheckpointStorage ： JobManager JVM Heap
  * Good for testing and experimentation with small state (locally)

### 4. Exactly Once Guarantees

* Flink makes no effort to recover from failures (at most once)
* Nothing is lost, but you may experience duplicated results (at least once)
* Nothing is lost or duplicated (exactly once)

> 精确一次不代表 每个事件只会处理一次。相反，他能保证每个事件，就算在异常恢复 的时候被倒回重新执行，
> 它对 被flink管理的状态的影响有且只会有一次。
> 精确一次保证需要对输入每个算子的数据流做 barrier alignment, 但是如果不需要精确
> 一次保证，就可以通过配置flink 为 CheckpointingMode.AT_LEAST_ONCE，来避免flink进行 barrier alignment,
> 可以得到性能提升
