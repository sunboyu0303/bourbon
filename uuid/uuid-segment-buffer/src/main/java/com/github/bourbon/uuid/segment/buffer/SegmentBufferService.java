package com.github.bourbon.uuid.segment.buffer;

import com.github.bourbon.base.extension.annotation.ExtensionScope;
import com.github.bourbon.base.extension.annotation.SPI;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/1 11:25
 */
@SPI(value = "default", scope = ExtensionScope.MODULE)
public interface SegmentBufferService {

    long getMaxId(long step);
}