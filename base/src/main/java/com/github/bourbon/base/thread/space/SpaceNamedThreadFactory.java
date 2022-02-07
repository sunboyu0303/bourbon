package com.github.bourbon.base.thread.space;

import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.thread.ThreadPoolGovernor;
import com.github.bourbon.base.utils.ObjectUtils;
import com.github.bourbon.base.utils.concurrent.NamedThreadFactory;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/29 16:03
 */
public final class SpaceNamedThreadFactory extends NamedThreadFactory {

    public SpaceNamedThreadFactory(String threadPoolName, String spaceName) {
        this(threadPoolName, spaceName, false);
    }

    public SpaceNamedThreadFactory(String threadPoolName, String spaceName, boolean daemon) {
        super(
                ObjectUtils.defaultSupplierIfNullElseFunction(System.getSecurityManager(), SecurityManager::getThreadGroup, () -> Thread.currentThread().getThreadGroup()),
                spaceName + StringConstants.HYPHEN + threadPoolName + StringConstants.HYPHEN + ThreadPoolGovernor.getInstance().getSpaceNameThreadPoolNumber(spaceName) + "-thread-",
                daemon
        );
    }
}