package com.github.bourbon.springframework.context.bean;

import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.LoggerFactory;
import com.github.bourbon.springframework.beans.factory.annotation.Autowired;
import com.github.bourbon.springframework.beans.factory.annotation.Value;
import com.github.bourbon.springframework.stereotype.Component;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/8 17:12
 */
@Component("userInjectService")
public class UserInjectService implements IUserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserInjectService.class);

    @Value("${token}")
    private String token;

    @Autowired
    private UserDao userDao;

    public String queryUserInfo() {
        try {
            TimeUnit.MILLISECONDS.sleep(new Random(1).nextInt(100));
        } catch (InterruptedException e) {
            LOGGER.error("", e);
        }
        return userDao.queryUserName("10001") + "，" + token;
    }

    public String register(String userName) {
        try {
            TimeUnit.MILLISECONDS.sleep(new Random(1).nextInt(100));
        } catch (InterruptedException e) {
            LOGGER.error("", e);
        }
        return "注册用户：" + userName + " success！";
    }

    @Override
    public String toString() {
        return "token=" + token;
    }
}