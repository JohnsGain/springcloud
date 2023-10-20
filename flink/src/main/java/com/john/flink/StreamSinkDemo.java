package com.john.flink;

import org.junit.jupiter.api.Test;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2023-10-17 03:24
 * @since jdk17
 */
public class StreamSinkDemo {

    /**
     * In production, commonly used sinks include the FileSink, various databases, and several pub-sub systems.
     *
     * In production, your application will run in a remote cluster or set of containers. And if it fails,
     * it will fail remotely. The JobManager and TaskManager logs can be very helpful in debugging such failures,
     * but it is much easier to do local debugging inside an IDE, which is something that Flink supports.
     * You can set breakpoints, examine local variables, and step through your code. You can also step into
     * Flink’s code, which can be a great way to learn more about its internals if you are curious to see
     * how Flink works.
     *
     */
    @Test
    public void test() {

    }
}
