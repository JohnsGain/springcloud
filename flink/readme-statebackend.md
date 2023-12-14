# State Backends

[参考](https://nightlies.apache.org/flink/flink-docs-release-1.18/docs/ops/state/state_backends/)

## Out of the box, Flink bundles these state backends:

### 1 HashMapStateBackend

HashMapStateBackend使用java堆内存存储
HashMapStateBackend is encouraged for:

* Jobs with large state, long windows, large key/value states.
* All high-availability setups.

### 2 EmbeddedRocksDBStateBackend

> EmbeddedRocksDBStateBackend 是以序列化的字节数组形式存储于 TaskManager的本地的数据目录，二进制序列化协议是根据配置的
> type serializer,

EmbeddedRocksDBStateBackend 限制

* As RocksDB’s JNI bridge API is based on byte[], the maximum supported size per key and per value is 2^31 bytes each.
  States that use merge operations in RocksDB (e.g. ListState) can silently accumulate value sizes > 2^31 bytes and will
  then fail on their next retrieval. This is currently a limitation of RocksDB JNI
* 解释： 单个key,value 最大限制2^31次方字节

EmbeddedRocksDBStateBackend is encouraged for:

* Jobs with very large state, long windows, large key/value states.
* All high-availability setups.

> **EmbeddedRocksDBStateBackend is currently the only backend that offers incremental checkpoints**

### 3 Choose The Right State Backend

> between HashMapStateBackend and RocksDB, it is a choice between performance and scalability. 