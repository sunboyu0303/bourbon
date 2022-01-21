package com.github.bourbon.ump.domain;

import com.alibaba.fastjson.JSON;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/27 00:27
 */
@Data
@Builder
public class JvmEnvironmentInfo implements Serializable {
    private static final long serialVersionUID = -2091186762881359827L;
    private int pid;
    private String jrev;
    private String osn;
    private String osa;
    private int osap;
    private String args;
    private String sp;
    private String st;
    private long tpms;
    private long tsss;
    private long cvms;
    private String ygcn;
    private String fgcn;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}