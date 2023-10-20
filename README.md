# springcloud
* springcloud practice
* springcloud practice
* springcloud practice124324325
* flink practive

### 新增flink 学习项目 2023-10-20
*基本概念*
* source
* sink
* operator :算子
* bounded stream
* unbounded stream
* JobManager 一对n
  * TaskManager 每个taskManger实际应用中就是不同的物理机或容器
    * task slot 每个task slot 就是数据流任务执行的独立并发线程
#### 基本代码
```java
public class DataStreamDemo1 {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        try (env) {
            DataStreamSource<Person> dataStreamSource = env.fromElements(
                    new Person("Fred", 35),
                    new Person("Wilma", 35),
                    new Person("Pebbles", 2),
                    new Person("Jimmy", 5)
            );
            SingleOutputStreamOperator<Person> adults = dataStreamSource.filter(item -> item.age > 18);

            adults.print();
            System.out.println("开始执行");
            env.execute();
        }
    }

}

```

### 新增activitidemo 2019-05-16
```java
@RestController
public class TestController {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private RepositoryService repositoryService;

    @GetMapping("get")
    public String get() {


        ProcessInstanceQuery processInstanceQuery = runtimeService.createProcessInstanceQuery();
        List<ProcessInstance> list = processInstanceQuery.list();
        return "helloworld" + list.size();
    }

    @GetMapping("deploy")
    public Result<List<Deployment>> list() {
        List<Deployment> list1 = repositoryService.createDeploymentQuery()
                .list();
        return new Result<>(list1);
    }
}
***
