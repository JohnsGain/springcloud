## flink 里面用到的序列化器
[官方文档](https://flink.apache.org/2020/04/15/flink-serialization-tuning-vol.-1-choosing-your-serializer-if-you-can/)

* PojoSerialize
* Tuple Data Types 
* Row Data Types
* Avro
  * Avro Specific 
  * Avro Generic
  * Avro Reflect
* Kryo
  * Disabling Kryo
* Apache Thrift (via Kryo)
* Protobuf (via Kryo)

## 支持 state schema evolution