package com.github.bourbon.bytecode.bytebuddy;

import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.LoggerFactory;
import com.github.bourbon.base.utils.MapUtils;
import com.github.bourbon.base.utils.TimeUnitUtils;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/10 23:22
 */
public class BizService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private Map<String, UserInfo> map = MapUtils.newHashMap();

    public boolean create(UserInfo userInfo) {
        if (map.containsKey(userInfo.getName())) {
            return false;
        }
        map.put(userInfo.getName(), userInfo);
        return true;
    }

    public boolean update(UserInfo userInfo) {
        if (!map.containsKey(userInfo.getName())) {
            return false;
        }
        map.put(userInfo.getName(), userInfo);
        return true;
    }

    public UserInfo read(String name) {
        return map.get(name);
    }

    public UserInfo delete(String name) {
        return map.remove(name);
    }

    public UserInfo queryUserInfo(String userId) {
        TimeUnitUtils.sleepMilliSeconds(ThreadLocalRandom.current().nextInt(500));
        logger.info("查询用户信息，userId：" + userId);
        return new UserInfo("用户:" + userId, 19, "天津市东丽区万科赏溪苑14-0000");
    }
}