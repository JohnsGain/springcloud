package eureka;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;

/**
 * @author ""
 * @date 2018/4/17
 * @description eureka服务
 * @since jdk1.8
 */
@EnableDiscoveryClient
@EnableHystrix
@SpringBootApplication
@MapperScan(basePackages = {"eureka.repository"})
public class EurekaClientApplication {

    public static void main(String[] args) {
        //LifeCycle
        SpringApplication.run(EurekaClientApplication.class, args);
    }
}




