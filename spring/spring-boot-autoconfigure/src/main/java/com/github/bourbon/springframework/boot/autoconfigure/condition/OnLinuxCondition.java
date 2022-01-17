package com.github.bourbon.springframework.boot.autoconfigure.condition;

import com.github.bourbon.base.system.SystemInfo;
import org.springframework.context.annotation.Condition;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/16 16:57
 */
class OnLinuxCondition extends SpringBootCondition implements Condition {

    @Override
    protected ConditionOutcome getMatchOutcome() {
        return getConditionOutcome(SystemInfo.osInfo.isLinux());
    }
}