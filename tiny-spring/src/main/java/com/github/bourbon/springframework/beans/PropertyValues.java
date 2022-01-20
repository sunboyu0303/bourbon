package com.github.bourbon.springframework.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/5 10:26
 */
public class PropertyValues {

    private final List<PropertyValue> propertyValueList = new ArrayList<>();

    public PropertyValues addPropertyValue(PropertyValue pv) {
        int len = propertyValueList.size();
        for (int i = 0; i < len; i++) {
            if (propertyValueList.get(i).getName().equals(pv.getName())) {
                propertyValueList.set(i, pv);
                return this;
            }
        }
        propertyValueList.add(pv);
        return this;
    }

    public PropertyValue[] getPropertyValues() {
        return propertyValueList.toArray(new PropertyValue[0]);
    }
}