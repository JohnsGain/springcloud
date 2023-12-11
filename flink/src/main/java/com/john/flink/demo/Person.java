package com.john.flink.demo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2023-10-17 02:56
 * @since jdk17
 */
@Data
@AllArgsConstructor
public class Person {

    public String name;
    public Integer age;

    public String country;

    private long eventTimestamp;

    public Person() {
        eventTimestamp = Instant.now().toEpochMilli();
    }

    public Person(String name, Integer age) {
        this.name = name;
        this.age = age;
        eventTimestamp = Instant.now().toEpochMilli();
    }

    public Person(String name, Integer age, String country) {
        this.name = name;
        this.age = age;
        this.country = country;
        eventTimestamp = Instant.now().toEpochMilli();
    }

}
