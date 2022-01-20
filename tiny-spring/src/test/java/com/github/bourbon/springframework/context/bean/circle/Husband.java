package com.github.bourbon.springframework.context.bean.circle;

import com.alibaba.fastjson.JSON;

import java.time.LocalDate;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/9 14:17
 */
public class Husband {

    private Wife wife;

    private String wifiName;

    private LocalDate marriageDate;

    public String queryWife() {
        return "Husband.wife";
    }

    public Wife getWife() {
        return wife;
    }

    public void setWife(Wife wife) {
        this.wife = wife;
    }

    public String getWifiName() {
        return wifiName;
    }

    public void setWifiName(String wifiName) {
        this.wifiName = wifiName;
    }

    public LocalDate getMarriageDate() {
        return marriageDate;
    }

    public void setMarriageDate(LocalDate marriageDate) {
        this.marriageDate = marriageDate;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}