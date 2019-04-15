package eureka.controller;

import eureka.common.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ""
 * @description 测试logback配置
 * @date 2018/9/30
 * @since jdk1.8
 */
@RestController
public class LogController {

    public static final Logger LOGGER = LoggerFactory.getLogger(LogController.class);

    @GetMapping("log")
    public Result<String> log() {
        LOGGER.debug("This is a debug message");
        LOGGER.info("This is an info message");
        LOGGER.warn("This is a warn message");
        LOGGER.error("This is an error message");
        return Result.<String>build().ok().withData("log-test");
    }
}
