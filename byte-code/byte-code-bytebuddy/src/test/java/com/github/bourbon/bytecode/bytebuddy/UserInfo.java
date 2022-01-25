package com.github.bourbon.bytecode.bytebuddy;

import com.github.bourbon.base.appender.builder.JsonStringBuilder;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/10 23:23
 */
public class UserInfo {
    private String code;
    private String info;

    private String name;
    private Integer age;
    private String address;

    public UserInfo(String name, Integer age, String address) {
        this.code = "0000";
        this.info = "success";
        this.name = name;
        this.age = age;
        this.address = address;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return new JsonStringBuilder(true)
                .appendBegin()
                .append("address", address)
                .append("age", age)
                .append("code", code)
                .append("info", info)
                .append("name", name)
                .appendEnd(false)
                .toString();
    }
}