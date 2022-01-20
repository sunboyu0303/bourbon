package com.github.bourbon.springframework.beans.bean;

import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/6 13:58
 */
public class UserDao implements IUserDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserDao.class);
    private static Map<String, String> hashMap = new HashMap<>();

    public void initDataMethod() {
        LOGGER.info("执行：init-method");
        hashMap.put("10001", "张三");
        hashMap.put("10002", "李四");
        hashMap.put("10003", "王五");
    }

    public void destroyDataMethod() {
        LOGGER.info("执行：destroy-method");
        hashMap.clear();
    }

    public String queryUserName(String uId) {
        return hashMap.get(uId);
    }
}