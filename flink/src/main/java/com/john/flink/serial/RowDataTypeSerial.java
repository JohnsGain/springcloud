package com.john.flink.serial;

import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.typeutils.ResultTypeQueryable;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.types.Row;
import org.junit.jupiter.api.Test;

/**
 * https://flink.apache.org/2020/04/15/flink-serialization-tuning-vol.-1-choosing-your-serializer-if-you-can/
 *
 * @author zhangjuwa
 * @apiNote
 * @date 2023-10-17 23:50
 * @since jdk17
 */
public class RowDataTypeSerial {
    /**
     * Row types are mainly used by the Table and SQL APIs of Flink. A Row groups an arbitrary number of objects
     * together similar to the tuples above. These fields are not strongly typed and may all be of different types.
     * Because field types are missing, Flink’s type extraction cannot automatically extract type information
     * and users of a Row need to manually tell Flink about the row’s field types. The RowSerializer will then
     * make use of these types for efficient serialization.
     * <p>
     * Row type information can be provided in two ways:
     */

    /**
     * 第一种
     * you can have your source or operator implement ResultTypeQueryable<Row>:
     * {@link MyRowSource}
     */
    @Test
    public void one() {
// 如果你没有为你的row 类型提供详细的类型说明，那么flink就会把它当做一个 不合法的pojo类型来处理，
//        也就是会 退回到 使用 Kryo序列化 来处理，性能就会差很多
    }

    /**
     * 第2种
     * you can provide the types when building the job graph by using SingleOutputStreamOperator#returns()
     */
    @Test
    public void two() {
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStream<Row> streamOperator = environment.addSource(new MyRowSource())
                .returns(Types.ROW(Types.BOOLEAN, Types.LONG, Types.STRING));
    }

    public static class MyRowSource implements SourceFunction<Row>, ResultTypeQueryable<Row> {

        @Override
        public TypeInformation<Row> getProducedType() {
            return Types.ROW(Types.STRING, Types.LONG, Types.BOOLEAN);
        }

        @Override
        public void run(SourceContext<Row> ctx) throws Exception {

        }

        @Override
        public void cancel() {

        }
    }

}
