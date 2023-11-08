package com.john.flink.demo;

import lombok.Data;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2023-10-17 02:56
 * @since jdk17
 */
@Data
public class Person {

    public String name;
    public Integer age;

    public String country;

    public Person() {
    }

    public Person(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

}
