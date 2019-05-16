# springcloud
springcloud practice
springcloud practice
springcloud practice124324325

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
