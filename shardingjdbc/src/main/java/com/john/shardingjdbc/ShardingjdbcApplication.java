package com.john.shardingjdbc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 官方已经实现的分片算法
 * https://shardingsphere.apache.org/document/current/cn/dev-manual/sharding/
 */
@SpringBootApplication
@Slf4j
public class ShardingjdbcApplication {

    public static void main(String[] args) {
        log.info(System.getProperty("java.version"));
        SpringApplication.run(ShardingjdbcApplication.class, args);
    }

}
