package com.github.bourbon.springframework.boot.context.properties.source;

import com.github.bourbon.base.constant.CharConstants;
import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.base.utils.ListUtils;
import com.github.bourbon.base.utils.ObjectUtils;
import com.github.bourbon.springframework.boot.context.properties.source.ConfigurationPropertyName.Form;

import java.util.List;
import java.util.Locale;
import java.util.function.BiPredicate;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/25 18:51
 */
final class SystemEnvironmentPropertyMapper implements PropertyMapper {

    public static final PropertyMapper INSTANCE = new SystemEnvironmentPropertyMapper();

    @Override
    public List<String> map(ConfigurationPropertyName configurationPropertyName) {
        String name = convertName(configurationPropertyName);
        String legacyName = convertLegacyName(configurationPropertyName);
        return BooleanUtils.defaultSupplierIfFalse(name.equals(legacyName), () -> ListUtils.newArrayList(name), () -> ListUtils.newArrayList(name, legacyName));
    }

    private String convertName(ConfigurationPropertyName name) {
        return convertName(name, name.getNumberOfElements());
    }

    private String convertName(ConfigurationPropertyName name, int numberOfElements) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < numberOfElements; i++) {
            if (result.length() > 0) {
                result.append(CharConstants.UNDERLINE);
            }
            result.append(name.getElement(i, Form.UNIFORM).toUpperCase(Locale.ENGLISH));
        }
        return result.toString();
    }

    private String convertLegacyName(ConfigurationPropertyName name) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < name.getNumberOfElements(); i++) {
            if (result.length() > 0) {
                result.append(CharConstants.UNDERLINE);
            }
            result.append(convertLegacyNameElement(name.getElement(i, Form.ORIGINAL)));
        }
        return result.toString();
    }

    private Object convertLegacyNameElement(String element) {
        return element.replace(CharConstants.HYPHEN, CharConstants.UNDERLINE).toUpperCase(Locale.ENGLISH);
    }

    @Override
    public ConfigurationPropertyName map(String propertySourceName) {
        return convertName(propertySourceName);
    }

    private ConfigurationPropertyName convertName(String propertySourceName) {
        try {
            return ConfigurationPropertyName.adapt(propertySourceName, CharConstants.UNDERLINE, this::processElementValue);
        } catch (Exception ex) {
            return ConfigurationPropertyName.EMPTY;
        }
    }

    private CharSequence processElementValue(CharSequence value) {
        String result = value.toString().toLowerCase(Locale.ENGLISH);
        return BooleanUtils.defaultIfFalse(isNumber(result), () -> StringConstants.LEFT_BRACKETS + result + StringConstants.RIGHT_BRACKETS, result);
    }

    private static boolean isNumber(String string) {
        return string.chars().allMatch(Character::isDigit);
    }

    @Override
    public BiPredicate<ConfigurationPropertyName, ConfigurationPropertyName> getAncestorOfCheck() {
        return this::isAncestorOf;
    }

    private boolean isAncestorOf(ConfigurationPropertyName name, ConfigurationPropertyName candidate) {
        return name.isAncestorOf(candidate) || isLegacyAncestorOf(name, candidate);
    }

    private boolean isLegacyAncestorOf(ConfigurationPropertyName name, ConfigurationPropertyName candidate) {
        if (!hasDashedEntries(name)) {
            return false;
        }
        return ObjectUtils.defaultIfNull(buildLegacyCompatibleName(name), l -> l.isAncestorOf(candidate), false);
    }

    private ConfigurationPropertyName buildLegacyCompatibleName(ConfigurationPropertyName name) {
        StringBuilder legacyCompatibleName = new StringBuilder();
        for (int i = 0; i < name.getNumberOfElements(); i++) {
            if (i != 0) {
                legacyCompatibleName.append(CharConstants.DOT);
            }
            legacyCompatibleName.append(name.getElement(i, Form.DASHED).replace(CharConstants.HYPHEN, CharConstants.DOT));
        }
        return ConfigurationPropertyName.ofIfValid(legacyCompatibleName);
    }

    boolean hasDashedEntries(ConfigurationPropertyName name) {
        for (int i = 0; i < name.getNumberOfElements(); i++) {
            if (name.getElement(i, Form.DASHED).indexOf(CharConstants.HYPHEN) != -1) {
                return true;
            }
        }
        return false;
    }
}