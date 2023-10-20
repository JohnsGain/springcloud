package com.john.shardingjdbc.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.john.shardingjdbc.domain.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author zhangjuwa  <a href="mailto:zhangjuwa@gmail.com">zhangjuwa</a>
 * @date 2023/8/8 00:51
 * @since jdk1.8
 */
@Slf4j
@Component
public class YgpMetaObjectHandler implements MetaObjectHandler {

    public YgpMetaObjectHandler() {
        log.info("### ygp-base init : YgpMetaObjectHandler ###");
    }

    /**
     * 插入时自动填充的逻辑方法
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        UserInfo userInfo = YgpContextHolder.getUserInfoWithDefault();
        LocalDateTime now = LocalDateTime.now();

        this.strictInsertFill(metaObject, "creator", String.class, userInfo.getNickname());
        this.strictInsertFill(metaObject, "creatorCode", String.class, userInfo.getUserCode());
        this.strictInsertFill(metaObject, "updater", String.class, userInfo.getNickname());
        this.strictInsertFill(metaObject, "updaterCode", String.class, userInfo.getUserCode());
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, now);
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, now);
    }

    /**
     * 更新时自动填充的逻辑方法
     * 这种方式，可以解决系统，直接select出来的 entity 然后直接更新的情况，
     * 导致 更新人，和更新时间没有发生变化
     * <p>
     * 但是 对于异步里面的情况 会设置会 admin的
     * 如：
     * 定时任务
     * 发布的 Listener
     * <p>
     * 这种情况 讨论之后 属于正常 情况。
     * 因为这个 数据确实 不是用户修改这个记录的某个字段，
     * 而是系统中触发的，所以设置为 admin 也是合理
     *
     * @author zhongyingguang
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        UserInfo userInfo = YgpContextHolder.getUserInfoWithDefault();
        this.setFieldValByName("updater", userInfo.getNickname(), metaObject);
        this.setFieldValByName("updaterCode", userInfo.getUserCode(), metaObject);
        this.setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
    }

}
