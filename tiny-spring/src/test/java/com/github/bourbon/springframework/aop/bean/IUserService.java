package com.github.bourbon.springframework.aop.bean;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/7 15:55
 */
public interface IUserService {

    String queryUserInfo();

    String register(String userName);
}