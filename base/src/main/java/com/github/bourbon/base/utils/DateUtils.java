package com.github.bourbon.base.utils;

import java.time.*;
import java.time.temporal.TemporalAccessor;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/18 14:44
 */
public interface DateUtils {

    static Instant toInstant(TemporalAccessor temporalAccessor) {
        if (temporalAccessor instanceof Instant) {
            return (Instant) temporalAccessor;
        } else if (temporalAccessor instanceof LocalDateTime) {
            return ((LocalDateTime) temporalAccessor).atZone(ZoneId.systemDefault()).toInstant();
        } else if (temporalAccessor instanceof ZonedDateTime) {
            return ((ZonedDateTime) temporalAccessor).toInstant();
        } else if (temporalAccessor instanceof OffsetDateTime) {
            return ((OffsetDateTime) temporalAccessor).toInstant();
        } else if (temporalAccessor instanceof LocalDate) {
            return ((LocalDate) temporalAccessor).atStartOfDay(ZoneId.systemDefault()).toInstant();
        } else if (temporalAccessor instanceof LocalTime) {
            return ((LocalTime) temporalAccessor).atDate(LocalDate.now()).atZone(ZoneId.systemDefault()).toInstant();
        } else if (temporalAccessor instanceof OffsetTime) {
            return ((OffsetTime) temporalAccessor).atDate(LocalDate.now()).toInstant();
        } else {
            return Instant.from(temporalAccessor);
        }
    }
}