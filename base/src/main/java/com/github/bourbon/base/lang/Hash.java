package com.github.bourbon.base.lang;

import com.github.bourbon.base.extension.annotation.ExtensionScope;
import com.github.bourbon.base.extension.annotation.SPI;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/23 12:03
 */
@SPI(scope = ExtensionScope.FRAMEWORK)
public interface Hash {

    int getHash(String str);
}