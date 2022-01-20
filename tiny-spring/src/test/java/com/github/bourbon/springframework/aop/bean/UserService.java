package com.github.bourbon.springframework.aop.bean;

import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.LoggerFactory;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/7 15:55
 */
public class UserService implements IUserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    public String queryUserInfo() {
        try {
            TimeUnit.MILLISECONDS.sleep(new Random(1).nextInt(100));
        } catch (InterruptedException e) {
            LOGGER.info("", e);
        }
        return "张三，100001，深圳";
    }

    public String register(String userName) {
        try {
            TimeUnit.MILLISECONDS.sleep(new Random(1).nextInt(100));
        } catch (InterruptedException e) {
            LOGGER.info("", e);
        }
        return "注册用户：" + userName + " success！";
    }
}