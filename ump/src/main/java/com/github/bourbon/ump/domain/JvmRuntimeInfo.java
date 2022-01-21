package com.github.bourbon.ump.domain;

import com.alibaba.fastjson.JSON;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/27 00:29
 */
@Data
@Builder
public class JvmRuntimeInfo implements Serializable {
    private static final long serialVersionUID = -6787802972379683591L;
    private int ptc;
    private int tc;
    private int dtc;
    private int lcc;
    private long tlcc;
    private long ucc;
    private long nhmu;
    private long hmu;
    private long inhmu;
    private long ihmu;
    private long cnhmu;
    private long chmu;
    private long mnhmu;
    private long mhmu;
    private long fgcc;
    private long ygcc;
    private long fgct;
    private long ygct;
    private double cpu;
    private double memory;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}