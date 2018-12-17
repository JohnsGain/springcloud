package com.shulian.bus.controller;

import com.shulian.bus.amqp.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author zhangjuwa
 * @description 測試rabbit生產消費
 * @date 2018/4/23
 * @since jdk1.8
 */
@RestController
@RequestMapping("/rabbit")
public class RabbitController {

    private final HelloSender1 helloSender1;

    private final HelloSender2 helloSender2;

    /**
     可以看出，使用构造器注入的方法，可以明确成员变量的加载顺序。
     Java变量的初始化顺序为：静态变量或静态语句块–>实例变量或初始化语句块–>构造方法–>@Autowired
     * @param helloSender1
     * @param helloSender2
     */
    @Autowired
    public RabbitController(HelloSender1 helloSender1, HelloSender2 helloSender2) {
        this.helloSender1 = helloSender1;
        this.helloSender2 = helloSender2;
    }

 /*   @Autowired
    private HelloSender2 helloSender2;*/

    @Autowired
    private UserSender userSender;

    @Autowired
    private TopicSender topicSender;
    @Autowired
    private FanoutSender fanoutSender;

    @GetMapping("/hello")
    public void hello() {
        helloSender1.send("hello1");
    }

    /**
     * 单生产者-多消费者
     */
    @GetMapping("/oneToMany")
    public void oneToMany() {
        for (int i = 0; i < 10; i++) {
            helloSender1.send("大噶好，我是渣渣辉！" + new Date());
        }
    }

    @GetMapping("/manyToMany")
    public void manyToMany() {
        for (int i = 0; i < 10; i++) {
            helloSender1.send("大噶好，我是渣渣辉！" + new Date());
            helloSender2.send("大噶好，我是古天乐！" + new Date());
        }
    }

    /**
     * 生产用户数据
     * @param user
     */
    @PostMapping("/user")
    public void sendUser(User user) {
        userSender.send(user);
    }

    /**
     * topic exchange类型rabbitmq测试
     */
    @GetMapping("/topic")
    public void topicTest() {
        topicSender.send();
    }

    /**
     * fanout exchange类型rabbitmq测试
     */
    @GetMapping("/fanout")
    public void fanoutTest() {
        fanoutSender.send();
    }
}
