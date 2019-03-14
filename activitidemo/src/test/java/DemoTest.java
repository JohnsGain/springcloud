import org.activiti.engine.RuntimeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author zhangjuwa
 * @date 2019/3/15
 * @since jdk1.8
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(DemoTest.class);

    @Autowired
    private RuntimeService runtimeService;
    @Test
    public void runtime() {

    }
}
