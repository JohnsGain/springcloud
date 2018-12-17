package com.shulian.kafka;

/**
 * @author zhangjuwa
 * @description logstash概念介绍
 * logstash 是一款开源的数据传输工具，具备实时数据传输能力。他可以把数据结构，大小，来源不一的数据进行收集，进行过滤，
 * 转化(比如把数据进行标准化处理)，
 * 然后再传输到特定的数据分析仓库中进行分析，统计，Logstash首选的数据仓库是elasticSearch。但是logstash也提供了其他的数据仓库
 * 输出接口。logstash的运行依赖jdk1.8环境，logstash通过挂号难道管道进行运作，管道有两个必须的元素，输入和输出，有一个可选元素
 * 过滤器，输入插件从指定的数据源抓取数据，过滤器插件把输入的数据按照指定的过滤器转换成用户想要的数据格式，以及一些数据加工工作，
 * 输出插件负责把过滤器处理完成的数据输出到数据分析仓库
 * @date 2018/10/8
 * @since jdk1.8
 */
public class LogstashProfile {
}
