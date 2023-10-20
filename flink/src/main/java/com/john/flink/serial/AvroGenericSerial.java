package com.john.flink.serial;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.typeutils.ResultTypeQueryable;
import org.apache.flink.formats.avro.typeutils.GenericRecordAvroTypeInfo;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.junit.jupiter.api.Test;

/**
 * Avro’s GenericRecord types cannot, unfortunately, be used automatically since they require the user to specify a schema
 * (either manually or by retrieving it from some schema registry). With that schema, you can provide the right type
 * information by either of the following options just like for the Row Types above:
 *
 * @author zhangjuwa
 * @apiNote
 * @date 2023-10-18 00:23
 * @since jdk17
 */
public class AvroGenericSerial {

    /**
     * 第一种
     * you can have your source or operator implement ResultTypeQueryable<Row>:
     * {@link AvroGenericSerial.AvroGenericSource}
     * just like for the Row Types above:
     */
    @Test
    public void one() {

    }

    @Test
    public void two() {
        GenericRecordAvroTypeInfo typeInfo = new GenericRecordAvroTypeInfo(Schema.create(Schema.Type.ARRAY));
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
        SingleOutputStreamOperator<GenericRecord> dataStream = environment.addSource(new AvroGenericSource(null))
                .returns(typeInfo);
    }


    public static class AvroGenericSource implements SourceFunction<GenericRecord>, ResultTypeQueryable<GenericRecord> {

        private final GenericRecordAvroTypeInfo producedType;

        public AvroGenericSource(Schema schema) {
            this.producedType = new GenericRecordAvroTypeInfo(schema);
        }

        @Override
        public TypeInformation<GenericRecord> getProducedType() {
            return producedType;
        }

        @Override
        public void run(SourceContext<GenericRecord> ctx) throws Exception {

        }

        @Override
        public void cancel() {

        }
    }
}




