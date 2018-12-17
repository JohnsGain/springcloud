package com.shulian.ribbon.entity;

/**
 * @author zhangjuwa
 * @date 2018/4/17
 * @description 用户
 * @since jdk1.8
 */
public class User {
    private String name;
    private Integer age;
    private Integer id;

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", id=" + id +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
