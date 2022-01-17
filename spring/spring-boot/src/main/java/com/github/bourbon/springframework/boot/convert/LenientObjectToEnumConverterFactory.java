package com.github.bourbon.springframework.boot.convert;

import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.lang.Assert;
import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.base.utils.MapUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.*;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/26 12:34
 */
abstract class LenientObjectToEnumConverterFactory<T> implements ConverterFactory<T, Enum<?>> {

    private static Map<String, List<String>> ALIASES;

    static {
        MultiValueMap<String, String> aliases = new LinkedMultiValueMap<>();
        aliases.add("true", "on");
        aliases.add("false", "off");
        ALIASES = MapUtils.unmodifiableMap(aliases);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <E extends Enum<?>> Converter<T, E> getConverter(Class<E> targetType) {
        Class<?> enumType = targetType;
        while (enumType != null && !enumType.isEnum()) {
            enumType = enumType.getSuperclass();
        }
        Assert.notNull(enumType, () -> "The target type " + targetType.getName() + " does not refer to an enum");
        return new LenientToEnumConverter<>((Class<E>) enumType);
    }

    @SuppressWarnings("unchecked")
    private class LenientToEnumConverter<E extends Enum> implements Converter<T, E> {

        private final Class<E> enumType;

        LenientToEnumConverter(Class<E> enumType) {
            this.enumType = enumType;
        }

        @Override
        public E convert(T source) {
            String value = source.toString().trim();
            return BooleanUtils.defaultIfFalse(!value.isEmpty(), () -> {
                try {
                    return (E) Enum.valueOf(enumType, value);
                } catch (Exception ex) {
                    return findEnum(value);
                }
            });
        }

        private E findEnum(String value) {
            String name = getCanonicalName(value);
            List<String> aliases = ALIASES.getOrDefault(name, Collections.emptyList());
            for (E candidate : (Set<E>) EnumSet.allOf(enumType)) {
                String candidateName = getCanonicalName(candidate.name());
                if (name.equals(candidateName) || aliases.contains(candidateName)) {
                    return candidate;
                }
            }
            throw new IllegalArgumentException("No enum constant " + enumType.getCanonicalName() + StringConstants.DOT + value);
        }

        private String getCanonicalName(String name) {
            StringBuilder canonicalName = new StringBuilder(name.length());
            name.chars().filter(Character::isLetterOrDigit).map(Character::toLowerCase).forEach(c -> canonicalName.append((char) c));
            return canonicalName.toString();
        }
    }
}