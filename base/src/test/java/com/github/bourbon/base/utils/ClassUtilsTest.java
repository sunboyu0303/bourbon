package com.github.bourbon.base.utils;

import com.github.bourbon.base.constant.CharConstants;
import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.LoggerFactory;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/19 12:24
 */
public class ClassUtilsTest {

    private final Logger logger = LoggerFactory.getLogger(ClassUtilsTest.class);

    @Test
    public void test() {
        logger.info(ClassUtils.lowerFirst(MetadataReaderFactory.class));
        logger.info(ClassUtils.lowerFirst(null));
        logger.info(ClassUtils.getPackageName(MetadataReaderFactory.class));
        logger.info(ClassUtils.getClassFileName(MetadataReaderFactory.class));
        Assert.assertEquals("metadataReaderFactory", ClassUtils.lowerFirst(MetadataReaderFactory.class));
        char c = 46;
        logger.info(c);
        Assert.assertEquals(c, CharConstants.DOT);
        Integer i = null;
        logger.info(ClassUtils.getClassName(i, true));
        i = 127;
        logger.info(ClassUtils.getClassName(i, true));
        logger.info(ClassUtils.getClassName(i, false));
        logger.info(ClassUtils.getPackageName(Integer.class));
        logger.info(ClassUtils.getClassFileName(Integer.class));
    }

    private class MetadataReaderFactory {
    }
}