import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhangjuwa
 * @description 测试基类
 * @date 2018/9/19
 * @since jdk1.8
 */
public abstract class BaseTest {

    protected static Logger logger = LoggerFactory.getLogger(BaseTest.class);

    static RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
    protected static CuratorFramework client = CuratorFrameworkFactory.newClient("192.168.2.111:2181", retryPolicy);

    static {
        client.start();
    }

    protected abstract String getPath();
}
