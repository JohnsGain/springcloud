package com.john.flink;

import org.apache.flink.api.common.ExecutionConfig;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.junit.jupiter.api.Test;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2023-10-17 23:17
 * @since jdk17
 */
public class Serialization1 {

    /**
     * 判断一个类是否是POJO，可以使用下面的代码检查
     */
    @Test
    public void test() {
        TypeInformation<Person> typeInformation = TypeInformation.of(Person.class);
        System.out.println(typeInformation.createSerializer(new ExecutionConfig()));
        // pojo  返回 PojoSerializer
        TypeInformation<NoPojoDo> typeInformation1 = TypeInformation.of(NoPojoDo.class);
        // 不是Pojo   返回  KryoSerializer
        System.out.println(typeInformation1.createSerializer(new ExecutionConfig()));
    }
}
