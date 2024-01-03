# Flink Architecture

> Flink is a distributed system and requires effective allocation and management of compute resources in order to
> execute streaming applications. It integrates with all common cluster resource managers such as Hadoop YARN and
> Kubernetes, but can also be set up to run as a standalone cluster or even as a library.

## 1.jobmanager：

> 包含3个组件:ResourceManager,Dispatcher,JobMaster

* 一个集群里面至少有一个jobmanager,高可用安装可以有多个，其中一个是主，其他是从
* 接收客户端发送的Job 并调度到 taskmanager
* 利用checkpoint coordinator 协调 taskmanager完成checkpoint
* 当出现failure, 负责让应用重新恢复执行
* 为每一个提交的job 分配一个jobmaster

## 2.taskmanager, 也叫做worker

* 一个集群至少有一个taskmanager，
* 在一个 taskmanager 资源调度的最小单元的 task slot,task slot的数量表明了资源的并行度
* 连接到jobmanager,报告自己的状态，接收job分配的job，按照配置可能并行拆分多个slot来执行Job
* 负责自己分配到的事件的在各个算子的状态数据存储，JVM内存或本地磁盘(rocksdb)

## 3.Tasks and Operator Chains
