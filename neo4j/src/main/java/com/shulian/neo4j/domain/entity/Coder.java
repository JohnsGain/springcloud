package com.shulian.neo4j.domain.entity;

import org.hibernate.validator.constraints.NotBlank;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;

/**
 * @author ""
 * @description 节点实体
 * @date 2018/9/5
 * @since jdk1.8
 */
@NodeEntity
public class Coder implements Serializable {
    private static final long serialVersionUID = -723401256935747235L;

    /**
     *  Neo4j会分配的ID
     */
    @GraphId
    private Long id;

    @Max(value = 1, message = "性别设置男1女0")
    @Min(value = 0, message = "性别设置男1女0")
    private byte sex;

    @NotBlank(message = "姓名不能为空")
    private String name;

    private String hobby;

    @Override
    public String toString() {
        return "Coder{" +
                "id=" + id +
                ", sex=" + sex +
                ", name='" + name + '\'' +
                ", hobby='" + hobby + '\'' +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte getSex() {
        return sex;
    }

    public void setSex(byte sex) {
        this.sex = sex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }
}
