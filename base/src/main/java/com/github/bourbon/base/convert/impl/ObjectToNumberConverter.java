package com.github.bourbon.base.convert.impl;

import com.github.bourbon.base.constant.CharConstants;
import com.github.bourbon.base.convert.ObjectConverter;
import com.github.bourbon.base.utils.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.temporal.TemporalAccessor;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.DoubleAdder;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.Function;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/18 14:33
 */
public class ObjectToNumberConverter implements ObjectConverter<Number> {

    private final Class<? extends Number> targetType;

    public ObjectToNumberConverter() {
        targetType = Number.class;
    }

    public ObjectToNumberConverter(Class<? extends Number> clazz) {
        targetType = ObjectUtils.defaultIfNull(clazz, Number.class);
    }

    @Override
    public Number convertInternal(Object value) {
        return convert(value, targetType, this::convertToStr);
    }

    static Number convert(Object value, Class<?> targetType, Function<Object, String> f) {
        if (value instanceof Enum) {
            return convert(((Enum) value).ordinal(), targetType, f);
        } else {
            String valueStr;
            if (Byte.class == targetType) {
                if (value instanceof Number) {
                    return ((Number) value).byteValue();
                }
                if (value instanceof Boolean) {
                    return BooleanUtils.toByte((Boolean) value);
                }
                valueStr = f.apply(value);
                try {
                    return BooleanUtils.defaultIfPredicate(valueStr, s -> !CharSequenceUtils.isBlank(s), Byte::parseByte);
                } catch (NumberFormatException e) {
                    return NumberUtils.parseNumber(valueStr).byteValue();
                }
            } else if (Short.class == targetType) {
                if (value instanceof Number) {
                    return ((Number) value).shortValue();
                }
                if (value instanceof Boolean) {
                    return BooleanUtils.toShort((Boolean) value);
                }
                if (value instanceof byte[]) {
                    return ByteUtils.bytesToShort(((byte[]) value));
                }
                valueStr = f.apply(value);
                try {
                    return BooleanUtils.defaultIfPredicate(valueStr, s -> !CharSequenceUtils.isBlank(s), Short::parseShort);
                } catch (NumberFormatException e) {
                    return NumberUtils.parseNumber(valueStr).shortValue();
                }
            } else if (Integer.class == targetType) {
                if (value instanceof Number) {
                    return ((Number) value).intValue();
                }
                if (value instanceof Boolean) {
                    return BooleanUtils.toInt((Boolean) value);
                }
                if (value instanceof Date) {
                    return (int) ((Date) value).getTime();
                }
                if (value instanceof Calendar) {
                    return (int) ((Calendar) value).getTimeInMillis();
                }
                if (value instanceof TemporalAccessor) {
                    return (int) DateUtils.toInstant((TemporalAccessor) value).toEpochMilli();
                }
                if (value instanceof byte[]) {
                    return ByteUtils.bytesToInt(((byte[]) value));
                }
                return BooleanUtils.defaultIfPredicate(f.apply(value), s -> !CharSequenceUtils.isBlank(s), NumberUtils::parseInt);
            } else {
                Number number;
                if (AtomicInteger.class == targetType) {
                    number = convert(value, Integer.class, f);
                    if (null != number) {
                        AtomicInteger intValue = new AtomicInteger();
                        intValue.set(number.intValue());
                        return intValue;
                    }
                } else {
                    if (Long.class == targetType) {
                        if (value instanceof Number) {
                            return ((Number) value).longValue();
                        }
                        if (value instanceof Boolean) {
                            return BooleanUtils.toLong((Boolean) value);
                        }
                        if (value instanceof Date) {
                            return ((Date) value).getTime();
                        }
                        if (value instanceof Calendar) {
                            return ((Calendar) value).getTimeInMillis();
                        }
                        if (value instanceof TemporalAccessor) {
                            return DateUtils.toInstant((TemporalAccessor) value).toEpochMilli();
                        }
                        if (value instanceof byte[]) {
                            return ByteUtils.bytesToLong(((byte[]) value));
                        }
                        return BooleanUtils.defaultIfPredicate(f.apply(value), s -> !CharSequenceUtils.isBlank(s), NumberUtils::parseLong);
                    }

                    if (AtomicLong.class == targetType) {
                        number = convert(value, Long.class, f);
                        if (null != number) {
                            AtomicLong longValue = new AtomicLong();
                            longValue.set(number.longValue());
                            return longValue;
                        }
                    } else if (LongAdder.class == targetType) {
                        number = convert(value, Long.class, f);
                        if (null != number) {
                            LongAdder longValue = new LongAdder();
                            longValue.add(number.longValue());
                            return longValue;
                        }
                    } else {
                        if (Float.class == targetType) {
                            if (value instanceof Number) {
                                return ((Number) value).floatValue();
                            }
                            if (value instanceof Boolean) {
                                return BooleanUtils.toFloat((Boolean) value);
                            }
                            if (value instanceof byte[]) {
                                return (float) ByteUtils.bytesToDouble(((byte[]) value));
                            }
                            return BooleanUtils.defaultIfPredicate(f.apply(value), s -> !CharSequenceUtils.isBlank(s), NumberUtils::parseFloat);
                        }

                        if (Double.class == targetType) {
                            if (value instanceof Number) {
                                return ((Number) value).doubleValue();
                            }
                            if (value instanceof Boolean) {
                                return BooleanUtils.toDouble((Boolean) value);
                            }
                            if (value instanceof byte[]) {
                                return ByteUtils.bytesToDouble(((byte[]) value));
                            }
                            return BooleanUtils.defaultIfPredicate(f.apply(value), s -> !CharSequenceUtils.isBlank(s), NumberUtils::parseDouble);
                        }

                        if (DoubleAdder.class == targetType) {
                            number = convert(value, Long.class, f);
                            if (null != number) {
                                DoubleAdder doubleAdder = new DoubleAdder();
                                doubleAdder.add(number.doubleValue());
                                return doubleAdder;
                            }
                        } else {
                            if (BigDecimal.class == targetType) {
                                return toBigDecimal(value, f);
                            }
                            if (BigInteger.class == targetType) {
                                return toBigInteger(value, f);
                            }
                            if (Number.class == targetType) {
                                if (value instanceof Number) {
                                    return (Number) value;
                                }
                                if (value instanceof Boolean) {
                                    return BooleanUtils.toInt((Boolean) value);
                                }
                                return BooleanUtils.defaultIfPredicate(f.apply(value), s -> !CharSequenceUtils.isBlank(s), NumberUtils::parseNumber);
                            }
                        }
                    }
                }
                throw new UnsupportedOperationException("Un support Number type: " + targetType.getName());
            }
        }
    }

    private static BigDecimal toBigDecimal(Object object, Function<Object, String> f) {
        if (object instanceof Number) {
            return NumberUtils.toBigDecimal((Number) object);
        }
        if (object instanceof Boolean) {
            return new BigDecimal((Boolean) object ? 1 : 0);
        }
        return BooleanUtils.defaultSupplierIfAssignableFrom(object, byte[].class,
                o -> NumberUtils.toBigDecimal(ByteUtils.bytesToDouble(((byte[]) o))),
                () -> NumberUtils.toBigDecimal(f.apply(object))
        );
    }

    private static BigInteger toBigInteger(Object object, Function<Object, String> f) {
        if (object instanceof Long) {
            return BigInteger.valueOf((Long) object);
        }
        if (object instanceof Boolean) {
            return BigInteger.valueOf((Boolean) object ? 1L : 0L);
        }
        return BooleanUtils.defaultSupplierIfAssignableFrom(object, byte[].class,
                o -> BigInteger.valueOf(ByteUtils.bytesToLong(((byte[]) o))),
                () -> NumberUtils.toBigInteger(f.apply(object))
        );
    }

    private String convertToStr(Object value) {
        String result = CharSequenceUtils.trim(ObjectConverter.convertToStr(value));
        if (!CharSequenceUtils.isEmpty(result)) {
            char c = Character.toUpperCase(result.charAt(result.length() - 1));
            if (c == CharConstants.D || c == CharConstants.L || c == CharConstants.F) {
                return CharSequenceUtils.subPre(result, -1);
            }
        }
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Class<Number> getTargetType() {
        return (Class<Number>) targetType;
    }
}