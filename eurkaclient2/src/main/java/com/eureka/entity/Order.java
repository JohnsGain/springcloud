package com.eureka.entity;

import java.io.Serializable;

/**
 * @author zhangjuwa
 * @description
 * @date 2018/9/17
 * @since jdk1.8
 */
public class Order implements Serializable {
    private static final long serialVersionUID = -3139302752024763416L;
    private Integer id;

    private Integer total;
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
