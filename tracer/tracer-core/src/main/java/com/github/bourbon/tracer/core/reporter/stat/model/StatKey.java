package com.github.bourbon.tracer.core.reporter.stat.model;

import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.base.utils.ObjectUtils;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/2/9 10:10
 */
public class StatKey {
    /**
     * Key for statistics
     */
    private String key;
    /**
     * Y success，N failure
     */
    private String result;
    /**
     * Whether it is pressure measurement stat
     */
    private boolean loadTest;
    /**
     * Printed end
     */
    private String end;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getEnd() {
        return end;
    }

    public boolean isLoadTest() {
        return loadTest;
    }

    public void setLoadTest(boolean loadTest) {
        this.loadTest = loadTest;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        StatKey statKey = (StatKey) o;
        if (!ObjectUtils.equals(key, statKey.key)) {
            return false;
        }
        if (loadTest != statKey.loadTest) {
            return false;
        }
        if (!ObjectUtils.equals(result, statKey.result)) {
            return false;
        }
        return ObjectUtils.equals(end, statKey.end);
    }

    @Override
    public int hashCode() {
        int result1 = ObjectUtils.defaultIfNullElseFunction(key, String::hashCode, 0);
        result1 = 31 * result1 + ObjectUtils.defaultIfNullElseFunction(result, String::hashCode, 0);
        result1 = 31 * result1 + BooleanUtils.defaultIfFalse(loadTest, 1, 0);
        result1 = 31 * result1 + ObjectUtils.defaultIfNullElseFunction(end, String::hashCode, 0);
        return result1;
    }
}