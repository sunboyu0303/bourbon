package com.github.bourbon.base.lang;

import com.github.bourbon.base.constant.LongConstants;
import org.junit.Test;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/13 16:49
 */
public class FileSizeTest {

    @Test
    public void test() {
        org.junit.Assert.assertEquals(FileSize.valueOf("1KB").getSize(), LongConstants.KB);
        org.junit.Assert.assertEquals(FileSize.valueOf("1GB").getSize(), LongConstants.GB);
    }
}