package eureka.config;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.transaction.CuratorTransactionResult;
import org.apache.curator.framework.api.transaction.TransactionOp;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.List;

/**
 * @author ""
 * @description 获取当前应用环境
 * @date 2018/4/20
 * @since jdk1.8
 */
@Configuration
public class EnviMyWebAppConfigurer implements EnvironmentAware {

    /**
     * 因为ZOoKeeper是一个共享的集群。所以命名空间约定极为重要，各个应用在使用同一集群时不会有冲突的ZK path。
     * CuratorFramework 提供了命名空间的概念。当生成CuratorFramework 可以设置命名空间。
     * CuratorFramework在调用API会在所有的path前面加上命名空间。
     * @return
     */
    @Bean
    public CuratorFramework curatorFramework() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.newClient("192.168.2.111:2181", retryPolicy)
                .usingNamespace("john");
        client.start();
        return client;
    }

    private Environment env;
    @Override
    public void setEnvironment(Environment environment) {
        this.env = environment;
    }

    public Environment getEnv() {
        return env;
    }
}
