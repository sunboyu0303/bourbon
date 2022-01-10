package com.github.bourbon.bytecode.asm;

import com.github.bourbon.base.lang.Clock;
import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.LoggerFactory;
import com.github.bourbon.base.utils.ListUtils;
import com.github.bourbon.base.utils.MapUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/10 19:49
 */
public class TestService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public Long getTime() throws IllegalArgumentException {
        return Clock.currentTimeMillis();
    }

    public long sum(long l1, long l2) throws Exception {
        return l1 + l2;
    }

    public Integer sum(Integer i1, Integer i2) throws IndexOutOfBoundsException {
        return i1 + i2;
    }

    public User getUser(String name) throws ClassCastException, InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100L);
        return new User();
    }

    public void saveUser(User user) throws NullPointerException {
        logger.error(user);
    }

    public Map<String, Integer> getMap(String key, Integer value) {
        return MapUtils.of(key, value);
    }

    public Double sum(Double d1, Double d2, Double d3) {
        return d1 + d2 + d3;
    }

    public int sum(int[] intArr) {
        int sum = 0;
        for (int i : intArr) {
            sum += i;
        }
        return sum;
    }

    public List<User> convert(User[] users) {
        return ListUtils.newArrayList(users);
    }
}