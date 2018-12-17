package eureka.config;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * @author zhangjuwa
 * @description 获取当前应用环境
 * @date 2018/4/20
 * @since jdk1.8
 */
@Configuration
public class EnviMyWebAppConfigurer implements EnvironmentAware {

    @Bean
    public CuratorFramework curatorFramework() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.newClient("192.168.2.111:2181", retryPolicy);
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
