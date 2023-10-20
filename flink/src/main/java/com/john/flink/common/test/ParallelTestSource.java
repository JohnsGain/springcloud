package com.john.flink.common.test;

import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.typeutils.ResultTypeQueryable;
import org.apache.flink.api.java.typeutils.TypeExtractor;
import org.apache.flink.streaming.api.functions.source.RichParallelSourceFunction;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2023-10-20 01:15
 * @since jdk17
 */
public class ParallelTestSource<T> extends RichParallelSourceFunction<T> implements ResultTypeQueryable<T> {

    private final T[] testStream;
    private final TypeInformation<T> typeInfo;

    public ParallelTestSource(T... array) {
        this.testStream = array;
        typeInfo = (TypeInformation<T>) TypeExtractor.createTypeInfo(array[0].getClass());
    }

    @Override
    public TypeInformation<T> getProducedType() {

        return typeInfo;
    }

    @Override
    public void run(SourceContext<T> ctx) throws Exception {
        int indexOfThisSubtask = getRuntimeContext().getIndexOfThisSubtask();
        int numberOfParallelSubtasks = getRuntimeContext().getNumberOfParallelSubtasks();
        int subtask = 0;
        // the elements of the testStream are assigned to the parallel instances in a round-robin
        // fashion
        for (T item : testStream) {
            if (subtask == indexOfThisSubtask) {
                ctx.collect(item);
            }
            subtask = (subtask + 1) % numberOfParallelSubtasks;
        }
        // test sources are finite, so they emit a Long.MAX_VALUE watermark when they finish
    }

    @Override
    public void cancel() {
        // ignore cancel, finite anyway
    }
}
