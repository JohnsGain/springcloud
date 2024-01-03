# 一些flink 实操 经验记录

## 停止Job的两种方式

* cancel,会尽快停止Job,如果算子在接到 cancel() 调用后没有停止，
  Flink 将开始定期中断算子线程的执行，直到所有算子停止为止。

* stop 更优雅的停止job，适用于source实现了 stopableFunction接口的job