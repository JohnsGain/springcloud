package com.shulian.kafka;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KGroupedStream;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 流API允许对来自一个或多个主题的消息进行连续计算，并将结果发送到零个，一个或多个主题中。
 *
 * @author ""
 * @description kafka数据流处理
 * @date 2018/9/21
 * @see {官方文档 https://kafka.apache.org/0102/javadoc/org/apache/kafka/streams/KafkaStreams.html}
 * @since jdk1.8
 */
public class KafkaStreamTest extends BaseTest {

    static Logger logger = LoggerFactory.getLogger(KafkaStreamTest.class);

    /**
     * 可以从Kafka获取某个主题的消息，经过处理后输出到另一个主题。
     * 相当于是对主题做了一个加工。比如从t1主题获取消息，
     * 然后计算数字的平方后发送消息到t2主题中。
     *
     * @param args
     */
    public static void main(String[] args) {
        //StreamsBuilder builder = new StreamsBuilder();
        //KStream<String, String> textLines = builder.stream(topic);
        //KTable<String, Long> wordCounts = textLines.flatMapValues(value -> Lists.newArrayList(value.split("\\W+")))
        //        .groupBy((k, v) -> v).count(Materialized.as("counts-store"));
        //wordCounts.toStream().foreach((key, value) -> {logger.info("key:{},value:{}", key, value);});
        //wordCounts.toStream().to(topic, Produced.with(Serdes.String(), Serdes.Long()));
        //KafkaStreams streams = new KafkaStreams(builder.build(), getConfig());
        //streams.start();

        //演示foreach方法
        //foreach();
        //演示filter
        //filter();

        //flatMap();

        //flatMapValue();

        groupby();
    }

    private static Properties getConfig() {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "wordcount-application");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.2.111:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        return props;
    }

    /**
     * 以流的方式KStream抓取主题中的数据，便利打印
     */
    private static void foreach() {
        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, String> textLinex = builder.stream(topic);
        textLinex.foreach((key, value) -> {
            logger.info("key:{},value:{}", key, value);
        });
        KafkaStreams streams = new KafkaStreams(builder.build(), getConfig());
       /* As a consequence, any fatal exception that happens during processing is by default only logged.
     * If you want to be notified about dying threads, you can
                * {@link #setUncaughtExceptionHandler(Thread.UncaughtExceptionHandler) register an uncaught exception handler}
                * */
       //start启动方法里面异步启动了线程执行，所以异步线程里面出现异常，程序不会阻塞，也不会报错，只有日志打印
        //要检测线程里面异常就要显式设置未catch的异常处理器
       streams.setUncaughtExceptionHandler((thread, exception) -> {
           logger.warn("线程:{}出现异常:{}", thread.getName(), exception.getMessage());
       });
        streams.start();
    }

    /**
     * 对得到的流数据进行过滤
     * 和filter相反的有filterNot()
     */
    private static void filter() {
        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, String> textLinex = builder.stream(topic);
        KStream<String, String> stream = textLinex.filter((key, value) -> value.length() <= 7);
        //stream = textLinex.filterNot((key, value) -> value.length() <= 7);
        stream.foreach((key, value) -> {
            logger.info("key:{},value:{}", key, value);
        });
        KafkaStreams streams = new KafkaStreams(builder.build(), getConfig());
        streams.start();
    }

    /**
     * 吧一条输入流记录转化为多条输出流记录,键和值都会改变
     */
    private static void flatMap() {
        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, String> stream = builder.stream(topic);
        KStream<String, Integer> outputStream = stream.flatMap((key, value) -> {
            String[] tokens = value.split(" ");
            List<KeyValue<String, Integer>> result = new ArrayList<>(tokens.length);
            for (String token : tokens) {
                result.add(new KeyValue<>(key + "_" + token, 1));
            }
            return result;
        });
        outputStream.foreach((key, value) -> logger.info("flatMap:{},{}", key, value));
        KafkaStreams streams = new KafkaStreams(builder.build(), getConfig());
        streams.start();
    }

    /**
     * 吧一条输入流记录转化为多条输出流记录,由同一条输入流记录生成的新记录，key保持原值
     */
    private static void flatMapValue() {
        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, String> stream = builder.stream(topic);
        //flatMapValues还有一个重载方法，有两个参数，key也作为参数传入，但是key本身是只读的
        KStream<String, String> outputStream = stream.flatMapValues(item -> {
            String[] tokens = item.split(" ");
            List<String> result = new ArrayList<>(tokens.length);
            for (String token : tokens) {
                result.add(token + "_" + token.length());
            }
            return result;
        });
        outputStream.foreach((key, value) -> logger.info("flatMapValue:{},{}", key, value));
        KafkaStreams streams = new KafkaStreams(builder.build(), getConfig());
        streams.start();
    }

    private static void groupby() {
        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, String> stream = builder.stream(topic);
        //按value长度进行分组
        KGroupedStream<Integer, String> group = stream.groupBy((key, value) -> value.length());
        //计算每个分组的个数
        
        KTable<Integer, Long> count = group.count();
        //count.toStream().foreach((key, value) -> logger.info("count:{},{}", key, value));

        KafkaStreams streams = new KafkaStreams(builder.build(), getConfig());
        streams.start();
    }

}
