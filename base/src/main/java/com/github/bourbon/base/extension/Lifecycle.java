package com.github.bourbon.base.extension;

import com.github.bourbon.base.lang.Prioritized;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/26 16:23
 */
public interface Lifecycle extends Prioritized {

    void initialize() throws Exception;

    void destroy() throws Exception;
}