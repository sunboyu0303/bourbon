package com.github.bourbon.springframework.boot.origin;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/19 15:13
 */
@FunctionalInterface
public interface OriginProvider {

    Origin getOrigin();
}