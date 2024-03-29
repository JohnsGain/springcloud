package com.john.flink.serial;

import com.john.flink.demo.NoPojoDo;
import com.john.flink.demo.Person;
import com.twitter.chill.protobuf.ProtobufSerializer;
import com.twitter.chill.thrift.TBaseSerializer;
import org.apache.flink.api.common.ExecutionConfig;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.junit.jupiter.api.Test;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2023-10-17 23:17
 * @since jdk17
 */
public class FlinkSerialization {

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

        TypeInformation<Tuple2<String, Integer>> typeInformation3 = TypeInformation.of(new TypeHint<Tuple2<String, Integer>>() {
        });
        System.out.println(typeInformation3.createSerializer(new ExecutionConfig()));

        // ====================  Row dataType ====================

        //  ====================  Avro  ====================
//        Flink offers built-in support for the Apache Avro serialization framework (currently using version 1.8.2)
//        by adding the org.apache.flink:flink-avro dependency into your job. Flink’s AvroSerializer can then use
//        Avro’s specific, generic, and reflective data serialization and make use of Avro’s performance and
//        flexibility, especially in terms of evolving the schema when the classes change over time.

//        Avro Specific  根据 SpecificRecordBase  定义自己的 avro类型

//        Avro Generic  GenericRecord，需要用户提供Schema, 没有类型信息，回回退到使用kyro,As a result,
//        the serialized form will be bigger and more costly to create.

//        Avro Reflect:The third way of using Avro is to exchange Flink’s PojoSerializer (for POJOs according to the rules above) for
//        Avro’s reflection-based serializer. This can be enabled by calling
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
        environment.getConfig()
                .enableForceAvro();

//        Kryo: 当你的对象或类没有被上面的所有序列化器识别到，那么flink就会用 kryo序列化器作为最终方案。
//        If you are using Kryo serialization, make sure to register your types with kryo:
        environment.getConfig()
                .registerKryoType(MyCustomType.class);

//        Registering types adds them to an internal map of classes to tags so that, during serialization,
//        Kryo does not have to add the fully qualified class names as a prefix into the serialized form.
//        Instead, Kryo uses these (integer) tags to identify the underlying classes and reduce serialization overhead

// !!!!!! Note Flink will store Kryo serializer mappings from type registrations in its checkpoints and
// savepoints and will retain them across job (re)starts.

//   Disabling Kryo: If desired, you can disable the Kryo fallback, i.e. the ability to serialize generic types, by calling
        // 也可以通过这个方式来检查哪些对象或类型没有设置好正确的类型，从而进行更完善的设置，用更高效的序列化器来替代kyro
        environment.getConfig()
                .disableGenericTypes();

//        Apache Thrift (via Kryo): 1. 需要么有禁用 kyro(disableGenericTypes). 2.对象类型是 Thrift-generated data type.
//        This only works if generic types are not disabled and MyCustomType is a Thrift-generated data type.
//        If the data type is not generated by Thrift, Flink will fail at runtime with an exception like this:
//java.lang.ClassCastException: class MyCustomType cannot be cast to class org.apache.thrift.TBase
// (MyCustomType and org.apache.thrift.TBase are in unnamed module of loader ‘app’)
        environment.getConfig()
                .addDefaultKryoSerializer(MyCustomType.class, TBaseSerializer.class);

//        Protobuf (via Kryo): 1. 需要么有禁用 kyro(disableGenericTypes). 2.对象类型是 Protobuf-generated class
        environment.getConfig()
                .registerTypeWithKryoSerializer(MyCustomType.class, ProtobufSerializer.class);

    }

    public static class MyCustomType {

    }

}
