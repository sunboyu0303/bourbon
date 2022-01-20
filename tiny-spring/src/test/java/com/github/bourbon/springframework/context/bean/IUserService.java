package com.github.bourbon.springframework.context.bean;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/8 14:49
 */
public interface IUserService {

    String queryUserInfo();

    String register(String userName);
}