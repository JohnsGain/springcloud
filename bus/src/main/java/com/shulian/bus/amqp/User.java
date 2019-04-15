package com.shulian.bus.amqp;

import java.io.Serializable;

/**
 * @author ""
 * @description amqp支持对象传输,对象必须实现序列化接口
 * @date 2018/4/24
 * @since jdk1.8
 */
public class User implements Serializable{
    private static final long serialVersionUID = 3459181234060532591L;

    private String name;
    private String pass;

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", pass='" + pass + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
