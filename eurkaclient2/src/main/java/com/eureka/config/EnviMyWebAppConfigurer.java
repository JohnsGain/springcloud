package com.eureka.config;

import org.springframework.context.EnvironmentAware;
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

    private Environment env;
    @Override
    public void setEnvironment(Environment environment) {
        this.env = environment;
    }

    public Environment getEnv() {
        return env;
    }
}
