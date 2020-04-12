package com.shulian.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author ""
 * @description 生产者客户端
 * 生产者包括一个缓冲区池，它保存尚未发送到服务器的记录，以及一个后台I/O线程，
 * 负责将这些记录转换为请求并将其传输到集群。使用后未能关闭生产者将泄漏这些资源。
 * 该send()方法是异步的。当被调用时，它将记录添加到待处理记录发送的缓冲区并立即返回。这允许生产者将各个记录收集在一起以获得效率。
 * <p>
 * acks配置其请求被视为完整性的标准。"all"意味着领导者将等待完整的同步副本来确认记录。
 * 只要至少有一个同步复制品仍然存在，这将保证记录不会丢失。这是最强大的保证。这相当于设置acks = -1。
 * @date 2018/9/20
 * @since jdk1.8
 */
public class ProducerTest extends BaseTest {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Properties props = new Properties();
        props.put("bootstrap.servers", "192.168.124.5:9092");
//        props.put("acks", "all");
        //如果请求失败，生产者可以自动重试,重试次数为0就不会重试，重试可能产生重复
        props.put("retries", 0);
        //生产者维护的每次发送消息的缓冲区
        props.put("batch.size", 16384);
        //指示生产者在发送请求之前等待该毫秒数
        props.put("linger.ms", 100);
       /*
            控制生产者可用于缓冲的总内存量。如果记录的发送速度比可以传输到服务器的速度快，
        那么这个缓冲空间就会耗尽。当缓冲区空间耗尽时，附加的发送呼叫将被阻塞。
        max.block.ms决定阻塞时间的阈值，超出此时间时，会引发TimeoutException。
        */
        props.put("buffer.memory", 33554432);
        props.put("key.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");

        Producer<String, String> producer = new KafkaProducer<String, String>(props);
        Random random = new Random();
        for (int i = 0; i < 1000; i++) {
//            Thread.sleep(300);
            String key = "key" + i;
//            String value = String.valueOf(random.nextInt(1000));
            String value = key;
            Future<RecordMetadata> test = producer.send(new ProducerRecord<>("test1", key, value));
            System.out.println(test.get().toString());
            //用于演示KStream的flatMap()
            //producer.send(new ProducerRecord<>("test", "key" + i, "Note that the console consumer currently enables offset"));
        }
//        new Thread(() -> {
//            for (int i = 0; i < 1000; i++) {
//                producer.send(new ProducerRecord<>("test7",
//                        String.valueOf(new Random().nextInt()), String.valueOf(System.nanoTime())));
//            }
//        }
//        )
//                .start();
//        producer.close();
    }

}
