package com.github.bourbon.bytecode.javassist;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/11 00:19
 */
public class BizServiceImpl implements BizService {

    @Override
    public Integer sum(String s1, String s2) {
        return Integer.parseInt(s1) + Integer.parseInt(s2);
    }
}