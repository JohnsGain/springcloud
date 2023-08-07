package com.john.shardingjdbc.domain;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author zhangjuwa  <a href="mailto:zhangjuwa@gmail.com">zhangjuwa</a>
 * @date 2023/8/8 00:47
 * @since jdk1.8
 */
@Data
public class UserInfo {
    private Long id;
    private String userCode;
    private String account;
    private String nickname;
    private String mobile;
    private Integer userType;
    private String email;
    private Integer tenantId;
    private Integer userIdentity;
    private String registerApproach;
    private LocalDateTime registractionTime;

    public static UserInfo defaultInfo() {
        UserInfo info = new UserInfo();
        info.setNickname("default");
        info.setUserCode("default");
        return info;
    }

}
