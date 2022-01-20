package com.github.bourbon.springframework.core.convert.support;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/10 11:39
 */
public class DefaultConversionService extends GenericConversionService {

    public DefaultConversionService() {
        addConverterFactory(new StringToNumberConverterFactory());
    }
}