package com.shulian.ribbon.servicce;

import com.shulian.ribbon.entity.User;

/**
 * @author zhangjuwa
 * @since jdk1.8
 */
public interface IComputeService {

    /**
     * 获取用户信息
     * @return
     */
    User getUser(Integer id);
}
