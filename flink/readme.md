# flink 学习

## connected join union 这几种可以合并流 的算子的区别

* connected : 可以合并不同类型的两个流，保留他们原本的类型

```java
DataStream<Integer> someStream=null;
        DataStream<String> otherStream=null;

        ConnectedStreams<Integer, String> connectedStreams=someStream.connect(otherStream);
```

* join 合并两个流，需要满足以各自某个字段的值相等 作为 合并条件
* union 可以对2个以及2个以上流进行合并