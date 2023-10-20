package com.john.shardingjdbc;

import com.john.shardingjdbc.config.YgpContextHolder;
import com.john.shardingjdbc.domain.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestContextManager;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * @description:
 * @author: pengdi
 * @date: 2020-06-29 19:20
 **/
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@WebAppConfiguration
@Slf4j
public class BaseTest {

    private TestContextManager testContextManager;


    @BeforeEach
    public void setUpContext() throws Exception {
        if (testContextManager == null) {
            this.testContextManager = new TestContextManager(getClass());
            this.testContextManager.prepareTestInstance(this);
        }
    }

    @BeforeEach
    public void beforeAllBase() {
        UserInfo ucUserDTO = new UserInfo();
        ucUserDTO.setUserCode("AD210304000042");
        ucUserDTO.setNickname("JOHN");
        YgpContextHolder.setUserInfo(ucUserDTO);
    }
//
//    @BeforeAll
//    public static void beforeAll() {
//        UserInfo ucUserDTO = new UserInfo();
//        ucUserDTO.setUserCode("AD201222000404");
//        ucUserDTO.setNickname("JOHN");
//        YgpContextHolder.setUserInfo(ucUserDTO);
//    }

}
