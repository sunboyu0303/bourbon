package com.github.bourbon.springframework.boot.autoconfigure.condition;

import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.lang.Assert;
import com.github.bourbon.base.utils.*;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/15 14:17
 */
public final class ConditionMessage {

    private final String message;

    private ConditionMessage() {
        this(null);
    }

    private ConditionMessage(String message) {
        this.message = message;
    }

    private ConditionMessage(ConditionMessage prior, String message) {
        this.message = BooleanUtils.defaultIfFalse(!prior.isEmpty(), () -> prior + StringConstants.SEMICOLON_SPACE + message, message);
    }

    public boolean isEmpty() {
        return CharSequenceUtils.isEmpty(message);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ConditionMessage)) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        return ObjectUtils.nullSafeEquals(((ConditionMessage) obj).message, message);
    }

    @Override
    public int hashCode() {
        return ObjectUtils.nullSafeHashCode(message);
    }

    @Override
    public String toString() {
        return ObjectUtils.defaultIfNull(message, StringConstants.EMPTY);
    }

    public ConditionMessage append(String message) {
        return BooleanUtils.defaultIfFalse(!CharSequenceUtils.isEmpty(message),
                () -> BooleanUtils.defaultSupplierIfFalse(CharSequenceUtils.isEmpty(message), () -> new ConditionMessage(message), () -> new ConditionMessage(this.message + StringConstants.SPACE + message)),
                this
        );
    }

    public Builder andCondition(Class<? extends Annotation> condition, Object... details) {
        Assert.notNull(condition, "Condition must not be null");
        return andCondition(StringConstants.AT + ClassUtils.getSimpleClassName(condition), details);
    }

    public Builder andCondition(String condition, Object... details) {
        Assert.notNull(condition, "Condition must not be null");
        String detail = ArrayUtils.toDelimitedString(details, StringConstants.SPACE);
        return BooleanUtils.defaultSupplierIfFalse(CharSequenceUtils.isEmpty(detail), () -> new Builder(condition), () -> new Builder(condition + StringConstants.SPACE + detail));
    }

    public static ConditionMessage empty() {
        return new ConditionMessage();
    }

    public static ConditionMessage of(String message, Object... args) {
        return BooleanUtils.defaultSupplierIfFalse(ArrayUtils.isEmpty(args), () -> new ConditionMessage(message), () -> new ConditionMessage(String.format(message, args)));
    }

    public static ConditionMessage of(Collection<? extends ConditionMessage> messages) {
        ConditionMessage result = new ConditionMessage();
        if (!CollectionUtils.isEmpty(messages)) {
            for (ConditionMessage message : messages) {
                result = new ConditionMessage(result, message.toString());
            }
        }
        return result;
    }

    public static Builder forCondition(Class<? extends Annotation> condition, Object... details) {
        return empty().andCondition(condition, details);
    }

    public static Builder forCondition(String condition, Object... details) {
        return empty().andCondition(condition, details);
    }

    public final class Builder {

        private final String condition;

        private Builder(String condition) {
            this.condition = condition;
        }

        public ConditionMessage foundExactly(Object result) {
            return found(StringConstants.EMPTY).items(result);
        }

        public ItemsBuilder found(String article) {
            return found(article, article);
        }

        public ItemsBuilder found(String singular, String plural) {
            return new ItemsBuilder(this, "found", singular, plural);
        }

        public ItemsBuilder didNotFind(String article) {
            return didNotFind(article, article);
        }

        public ItemsBuilder didNotFind(String singular, String plural) {
            return new ItemsBuilder(this, "did not find", singular, plural);
        }

        public ConditionMessage resultedIn(Object result) {
            return because("resulted in " + result);
        }

        public ConditionMessage available(String item) {
            return because(item + " is available");
        }

        public ConditionMessage notAvailable(String item) {
            return because(item + " is not available");
        }

        public ConditionMessage because(String reason) {
            return BooleanUtils.defaultSupplierIfFalse(CharSequenceUtils.isEmpty(reason), () -> new ConditionMessage(ConditionMessage.this, condition),
                    () -> new ConditionMessage(ConditionMessage.this, BooleanUtils.defaultIfFalse(!CharSequenceUtils.isEmpty(condition), () -> condition + StringConstants.SPACE + reason, reason))
            );
        }
    }

    public final class ItemsBuilder {

        private final Builder condition;
        private final String reason;
        private final String singular;
        private final String plural;

        private ItemsBuilder(Builder condition, String reason, String singular, String plural) {
            this.condition = condition;
            this.reason = reason;
            this.singular = singular;
            this.plural = plural;
        }

        public ConditionMessage atAll() {
            return items(Collections.emptyList());
        }

        public ConditionMessage items(Object... items) {
            return items(Style.NORMAL, items);
        }

        public ConditionMessage items(Style style, Object... items) {
            return items(style, ObjectUtils.defaultIfNullElseFunction(items, Arrays::asList, (Collection<?>) null));
        }

        public ConditionMessage items(Collection<?> items) {
            return items(Style.NORMAL, items);
        }

        public ConditionMessage items(Style style, Collection<?> items) {
            Assert.notNull(style, "Style must not be null");

            StringBuilder sb = new StringBuilder(reason);
            items = style.applyTo(items);

            if ((condition == null || items == null || items.size() <= 1) && !CharSequenceUtils.isEmpty(singular)) {
                sb.append(StringConstants.SPACE).append(singular);
            } else if (!CharSequenceUtils.isEmpty(plural)) {
                sb.append(StringConstants.SPACE).append(plural);
            }

            if (!CollectionUtils.isEmpty(items)) {
                sb.append(StringConstants.SPACE).append(CollectionUtils.toDelimitedString(items, StringConstants.COMMA_SPACE));
            }

            return ObjectUtils.defaultSupplierIfNullElseFunction(condition, c -> c.because(sb.toString()), () -> new ConditionMessage(sb.toString()));
        }
    }

    public enum Style {
        NORMAL {
            @Override
            protected Object applyToItem(Object item) {
                return item;
            }
        },
        QUOTE {
            @Override
            protected String applyToItem(Object item) {
                return ObjectUtils.defaultIfNullElseFunction(item, i -> StringConstants.SINGLE_QUOTE + i + StringConstants.SINGLE_QUOTE);
            }
        };

        public Collection<?> applyTo(Collection<?> items) {
            return ObjectUtils.defaultIfNullElseFunction(items, i -> i.stream().map(this::applyToItem).collect(Collectors.toList()));
        }

        protected abstract Object applyToItem(Object item);
    }
}