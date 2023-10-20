package com.john.shardingjdbc.config;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.john.shardingjdbc.domain.UserInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * @author zhangjuwa  <a href="mailto:zhangjuwa@gmail.com">zhangjuwa</a>
 * @date 2023/8/8 00:49
 * @since jdk1.8
 */
public class YgpContextHolder {
    private static ThreadLocal<Map<String, Object>> local = new TransmittableThreadLocal();

    private YgpContextHolder() {
    }

    public static void setToken(String token) {
        setValue("token", token);
    }

    public static void setUserInfo(UserInfo userInfo) {
        setValue("userInfo", userInfo);
    }

    public static String getToken() {
        Object obj = getValue("token");
        return Objects.isNull(obj) ? null : String.valueOf(obj);
    }

    public static UserInfo getUserInfo() {
        Object obj = getValue("userInfo");
        return (UserInfo) obj;
    }

    public static UserInfo getUserInfoWithDefault() {
        Object obj = getValue("userInfo");
        return Objects.isNull(obj) ? UserInfo.defaultInfo() : (UserInfo) obj;
    }

    public static UserInfo getUserInfo(Supplier<UserInfo> supplier) {
        Object obj = getValue("userInfo");
        return Objects.isNull(obj) ? (UserInfo) supplier.get() : (UserInfo) obj;
    }

    private static Object getValue(String key) {
        Map<String, Object> map = (Map) local.get();
        return Objects.isNull(map) ? null : map.get(key);
    }

    private static void setValue(String key, Object value) {
        Map<String, Object> map = (Map) local.get();
        if (Objects.isNull(map)) {
            map = new HashMap(4);
        }

        ((Map) map).put(key, value);
        local.set(map);
    }

    public static void remove() {
        local.remove();
    }
}
