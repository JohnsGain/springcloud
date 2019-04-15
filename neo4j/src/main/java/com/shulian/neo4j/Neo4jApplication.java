package com.shulian.neo4j;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author ""
 * @description 启动
 * @date 2018/9/5
 * @since jdk1.8
 */
@SpringBootApplication
@EnableDiscoveryClient
@EntityScan({"com.shulian.neo4j.domain.entity"})
public class Neo4jApplication {

    public static void main(String[] args) {
        SpringApplication.run(Neo4jApplication.class, args);
    }
}
