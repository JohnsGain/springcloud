package com.shulian.sleuth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import zipkin.server.EnableZipkinServer;

/**
 * @author zhangjuwa
 * @Description:
 * @date 2018/5/20
 * @since jdk1.8
 */
@SpringBootApplication
@EnableZipkinServer
public class SleuthServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(SleuthServerApplication.class, args);
    }
}
