package com.john.flink.demo;

import org.apache.flink.runtime.state.hashmap.HashMapStateBackend;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2023-12-14 00:31
 * @since jdk17
 */
public class StateBackendDemo {

    /**
     * Configuring a State Backend
     * The default state backend, if you specify nothing, is the jobmanager.
     * If you wish to establish a different default for all jobs on your cluster,
     * you can do so by defining a new default state backend in flink-conf.yaml.
     * The default state backend can be overridden on a per-job basis, as shown below.
     *
     * @param args
     */
    public static void main(String[] args) {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setStateBackend(new HashMapStateBackend());
    }

}
