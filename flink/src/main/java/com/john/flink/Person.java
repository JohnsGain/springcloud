package com.john.flink;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2023-10-17 02:56
 * @since jdk17
 */
public class Person {

    public String name;
    public Integer age;

    public Person() {
    }

    public Person(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public String toString() {
        return this.name.toString() + ": age " + this.age.toString();
    }
}
