package com.github.bourbon.springframework.boot.autoconfigure.condition;

import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.lang.Assert;
import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.base.utils.ObjectUtils;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/15 11:17
 */
public class ConditionOutcome {

    private final boolean match;

    private final ConditionMessage message;

    private ConditionOutcome(boolean match, String message) {
        this(match, ConditionMessage.of(message));
    }

    private ConditionOutcome(boolean match, ConditionMessage message) {
        Assert.notNull(message, "ConditionMessage must not be null");
        this.match = match;
        this.message = message;
    }

    public static ConditionOutcome match() {
        return match(ConditionMessage.empty());
    }

    public static ConditionOutcome match(String message) {
        return of(true, message);
    }

    public static ConditionOutcome match(ConditionMessage message) {
        return of(true, message);
    }

    public static ConditionOutcome noMatch() {
        return noMatch(ConditionMessage.empty());
    }

    public static ConditionOutcome noMatch(String message) {
        return of(false, message);
    }

    public static ConditionOutcome noMatch(ConditionMessage message) {
        return of(false, message);
    }

    public boolean isMatch() {
        return match;
    }

    public String getMessage() {
        return BooleanUtils.defaultIfFalse(!message.isEmpty(), message::toString);
    }

    public ConditionMessage getConditionMessage() {
        return message;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        return BooleanUtils.defaultSupplierIfFalse(getClass() == obj.getClass(), () -> {
            ConditionOutcome o = (ConditionOutcome) obj;
            return match == o.match && ObjectUtils.nullSafeEquals(message, o.message);
        }, () -> super.equals(obj));
    }

    @Override
    public int hashCode() {
        return BooleanUtils.hashCode(match) * 31 + ObjectUtils.nullSafeHashCode(message);
    }

    @Override
    public String toString() {
        return ObjectUtils.defaultIfNullElseFunction(message, ConditionMessage::toString, StringConstants.EMPTY);
    }

    public static ConditionOutcome of(boolean match, String message) {
        return new ConditionOutcome(match, message);
    }

    public static ConditionOutcome of(boolean match, ConditionMessage message) {
        return new ConditionOutcome(match, message);
    }

    public static ConditionOutcome inverse(ConditionOutcome outcome) {
        return of(!outcome.isMatch(), outcome.getConditionMessage());
    }
}