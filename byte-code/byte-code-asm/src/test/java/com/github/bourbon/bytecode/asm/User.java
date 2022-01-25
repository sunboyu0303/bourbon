package com.github.bourbon.bytecode.asm;

import com.github.bourbon.base.appender.builder.JsonStringBuilder;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/10 19:50
 */
public class User {
    private String name;
    private int age;

    public User() {
    }

    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return new JsonStringBuilder(true).appendBegin().append("name", name).append("age", age).appendEnd(false).toString();
    }
}