package com.ygp.batch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2022/1/6 11:32
 * @since jdk1.8
 */
@Configuration
public class JobConfig implements BatchConfigurer {

    @Resource
    private DataSource dataSource;

    @Resource
    private PlatformTransactionManager platformTransactionManager;


    @Bean
    public Job footballJob() throws Exception {
        JobRepository jobRepository = getJobRepository();

        return
    }

    @Override
    public JobRepository getJobRepository() throws Exception {
        JobRepositoryFactoryBean factoryBean = new JobRepositoryFactoryBean();

        factoryBean.setDataSource(dataSource);
        factoryBean.setTransactionManager(platformTransactionManager);
        factoryBean.setTablePrefix("batch_");
        factoryBean.setMaxVarCharLength(1000);
        return factoryBean.getObject();
    }

    @Override
    public PlatformTransactionManager getTransactionManager() throws Exception {
        return platformTransactionManager;
    }

    @Override
    public JobLauncher getJobLauncher() throws Exception {
        return null;
    }

    @Override
    public JobExplorer getJobExplorer() throws Exception {
        return null;
    }
}
