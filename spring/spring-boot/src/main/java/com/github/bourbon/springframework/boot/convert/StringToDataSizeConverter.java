package com.github.bourbon.springframework.boot.convert;

import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.base.utils.ObjectUtils;
import com.github.bourbon.base.utils.SetUtils;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.util.unit.DataSize;
import org.springframework.util.unit.DataUnit;

import java.util.Set;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/26 12:19
 */
final class StringToDataSizeConverter implements GenericConverter {

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return SetUtils.newHashSet(new ConvertiblePair(String.class, DataSize.class));
    }

    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        return BooleanUtils.defaultIfFalse(!ObjectUtils.isEmpty(source), () -> convert(source.toString(), getDataUnit(targetType)));
    }

    private DataSize convert(String source, DataUnit unit) {
        return DataSize.parse(source, unit);
    }
}