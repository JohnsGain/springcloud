package com.shulian.kafka;

/**
 * @author zhangjuwa
 * @description kafka简介
 * @see {机制和应用场景好文: https://blog.csdn.net/code52/article/details/50475511}
 * @see {大神制作 https://blog.csdn.net/isea533/article/details/73720066}
 * @see {集群模式配置 https://www.cnblogs.com/luotianshuai/p/5206662.html}
 * @date 2018/9/9
 * @since jdk1.8
 */
public class KafkaProfile {
    // 1.kafka是一个分布式的消息传递系统，支持pub-sub，队列的消息传递模式。他具有处理TB级别数据的承载能力，
    // 2.它最初由Linkedln公司开发，使用Scala语言编写，之后成为Apache项目的一部分,
    //  用作LinkedIn的活动流（Activity Stream）和运营数据处理管道（Pipeline）的基础。
    //  活动流数据是很多互联网公司做统计的一个重要数据组成部分，活动流数据包括
    //  网站每天访问量，每个模块点击频率，搜索情况等，这种数据通常处理方式是先把各种活动以某种形式文件记录下来，
    //  然后对这些数据进行抓取统计分析;运营数据是指服务器的CPU,IO使用率，请求时间，服务日志等数据
   /* 3.使用场景 消息传递(目前没有支持事务，不保证消息传递可靠性)
        日志收集
        网站使用追踪
    Metrics*/
   /* 通过一个Leader多个Follower模式来处理数据备份，同时在Leader挂掉之后，从Follower中任命新的Leader，
    来做到数据的一致性，安全性，从而达到Kafka的高可用性，分区容错性标准。允许将信息持久化到磁盘，防止数据丢失。
    kAFKA平均一个分区每秒几十万的读写，单个Broker
    每秒200万的写操作，每个主题的各个分区可以在不同的机器上，不同的Broker里面，可以同时被订阅了消息的COnsumer在不同的分区消费，
    也说明的kafka在高性能具备的可能。支持online（上线）和offline（下线）的场景

    启动kafka服务,进入到安装包目录下, bin/kafka-server-start.sh config/server.properties &
    停止kafka服务 ：bin/kafka-server-stop.sh

    单机环境连通性测试：把生产者生产的信息打印到控制台
1. 在linux开一个生产者窗口，执行以下命令，标准输入方式，--broker-list,--topic两个参数必须带上
bin/kafka-console-producer.sh --broker-list 192.168.2.111:9092 --topic test

从文件产出内容
 bin/kafka-console-producer.sh --broker-list 192.168.2.111:9092 --topic test < file-input.txt

2. 在linux新开一个消费者窗口，执行以下命令，--topic 指定主题,--from-beginning 指定从头查看消息
bin/kafka-console-consumer.sh --bootstrap-server 192.168.2.111:9092 --topic test --from-beginning
3. 在生产者窗口输入字符，按enter，此时消费者窗口会打印出接收到的消息

一些简单主题命令行:
1. 查看主题信息
bin/kafka-topics.sh --describe --zookeeper localhost:2181 --topic test
2. 添加分区
bin/kafka-topics.sh --alter --zookeeper localhost:2181 --topic test_topic --partitions 3
3. 删除主题
bin/kafka-topics.sh --delete --zookeeper 192.168.2.111:2181 --topic test

查看出主题列表
bin/kafka-topics.sh --zookeeper 127.0.0.1:2181 --list
查看当前主题下的消费组列表
bin/kafka-conusmer-groups.sh --bootstrap-server 192.168.2.111:9092 --list

创建主题
bin/kafka-topics.sh --create --zookeeper 10.200.250.194:2181,10.200.250.195:2181 --replication-factor 1 --partitions 1 --topic test

1. 从命令行中获取要创建的topic名称
2. 解析命令行指定的topic配置(如果存在的话)，配置都是x=a的格式
3. 若指定了replica-assignment参数表明用户想要自己分配分区副本与broker的映射——通常都不这么做，如果不提供该参数Kafka帮你做这件事情
4. 检查必要的参数是否已指定，包括：zookeeper， replication-factor，partition和topic
5. 获取/brokers/ids下所有broker并按照broker id进行升序排序
6. 在broker上分配各个分区的副本映射 (没有指定replica-assignment参数，这也是默认的情况)
7. 检查topic名字合法性、自定义配置的合法性，并且要保证每个分区都必须有相同的副本数
8. 若zookeeper上已有对应的路径存在，直接抛出异常表示该topic已经存在
9. 确保某个分区的多个副本不会被分配到同一个broker
10. 若提供了自定义的配置，更新zookeeper的/config/topics/[topic]节点的数据
11. 创建/brokers/topics/[topic]节点，并将分区副本分配映射数据写入该节点

bin/kafka-consumer-groups.sh --bootstrap-server localhost:9092 --describe --group my-group
    */

}
