### the execution mode can be configured via the execution.runtime-mode setting. There are three possible values:

* STREAMING: The classic DataStream execution mode (default)
* BATCH: Batch-style execution on the DataStream API
* AUTOMATIC: Let the system decide based on the boundedness of the sources

设置flink 执行模式的2种方式：

bin/flink run -Dexecution.runtime-mode=BATCH <jarFile>

```java
public class dd {
    public void dd() {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setRuntimeMode(RuntimeExecutionMode.BATCH);
    }
}

```