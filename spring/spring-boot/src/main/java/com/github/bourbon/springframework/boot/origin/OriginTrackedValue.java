package com.github.bourbon.springframework.boot.origin;

import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.base.utils.ObjectUtils;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/21 14:07
 */
public class OriginTrackedValue implements OriginProvider {

    private final Object value;

    private final Origin origin;

    private OriginTrackedValue(Object value, Origin origin) {
        this.value = value;
        this.origin = origin;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public Origin getOrigin() {
        return origin;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }
        return ObjectUtils.nullSafeEquals(value, ((OriginTrackedValue) obj).value);
    }

    @Override
    public int hashCode() {
        return ObjectUtils.nullSafeHashCode(value);
    }

    @Override
    public String toString() {
        return ObjectUtils.defaultIfNullElseFunction(value, Object::toString);
    }

    public static OriginTrackedValue of(Object value) {
        return of(value, null);
    }

    public static OriginTrackedValue of(Object object, Origin origin) {
        return ObjectUtils.defaultIfNullElseFunction(object, value -> BooleanUtils.defaultSupplierIfAssignableFrom(
                value, CharSequence.class, v -> new OriginTrackedCharSequence((CharSequence) v, origin), () -> new OriginTrackedValue(value, origin)
        ));
    }

    private static class OriginTrackedCharSequence extends OriginTrackedValue implements CharSequence {

        OriginTrackedCharSequence(CharSequence value, Origin origin) {
            super(value, origin);
        }

        @Override
        public int length() {
            return getValue().length();
        }

        @Override
        public char charAt(int index) {
            return getValue().charAt(index);
        }

        @Override
        public CharSequence subSequence(int start, int end) {
            return getValue().subSequence(start, end);
        }

        @Override
        public CharSequence getValue() {
            return (CharSequence) super.getValue();
        }
    }
}