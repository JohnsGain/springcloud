package com.shulian.kafka;

/**
 * @see {官网 https://kafka.apache.org/documentation/#configuration}
 * @author zhangjuwa
 * @description kafka流处理基础知识
 * @date 2018/9/26
 * @since jdk1.8
 */
public class KafkaStreamProfile {
    /*
        stream:它代表了一个无界，持续更新的数据集。一个流是一个有序，可重复读取，容错的不可变数据记录序列，
    一个数据记录被定义为一个键值对（key-value pair）。

        Kstream:事件流，数据的记录的方式是追加（insert），后面的数据会追加到之前的数据里。可以理解为你的Kafka Stream读取topic数
    据时，就是存储在这里的。     

        Ktable：changelog流，数据的记录的方式是更新（update），相同的key后面的数据会覆盖掉前面的数据。PS:由于Kstream和Ktable
    这两种特性，我们可以知道Kstream是不安全的，因为一旦日志数据出现压缩了，之前的key值就被删除了。这样进入的数据方式就变成了更新。

    */
}
