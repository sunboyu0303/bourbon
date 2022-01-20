package com.github.bourbon.springframework.context.bean;

import com.github.bourbon.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/8 17:11
 */
@Component
public class UserDao {
    private static Map<String, String> hashMap = new HashMap<>();

    static {
        hashMap.put("10001", "张三");
        hashMap.put("10002", "李四");
        hashMap.put("10003", "王五");
    }

    public String queryUserName(String uId) {
        return hashMap.get(uId);
    }
}