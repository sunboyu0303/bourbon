package com.github.bourbon.springframework.beans;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/5 10:30
 */
public class PropertyValue {

    private final String name;

    private Object value;

    private PropertyValue(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public static PropertyValue of(String name, Object value) {
        return new PropertyValue(name, value);
    }
}