# Fault-Tolerance

[参考](https://nightlies.apache.org/flink/flink-docs-master/docs/learn-flink/fault_tolerance/)

### 2. Exactly Once End-to-end #

To achieve exactly once end-to-end, so that every event from the sources affects the sinks exactly once, the following
must be true:

* your sources must be replayable, and
* your sinks must be transactional (or idempotent)

### Checkpoint Storage

The location where these snapshots are stored is defined via the jobs checkpoint storage. Two implementations of
checkpoint storage are available

* FileSystemCheckpointStorage ： Distributed file system
  * Supports very large state size
  * Highly durable
  * Recommended for production deployments
* JobManagerCheckpointStorage ： JobManager JVM Heap
  * Good for testing and experimentation with small state (locally)