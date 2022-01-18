package com.github.bourbon.base.utils.function.throwable;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/11 16:33
 */
@FunctionalInterface
public interface ThrowableAction {

    void execute() throws Exception;

    static void execute(ThrowableAction action) throws IllegalArgumentException {
        try {
            action.execute();
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}