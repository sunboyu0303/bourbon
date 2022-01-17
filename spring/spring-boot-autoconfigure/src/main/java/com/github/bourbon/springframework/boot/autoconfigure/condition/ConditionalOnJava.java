package com.github.bourbon.springframework.boot.autoconfigure.condition;

import com.github.bourbon.base.system.JavaVersion;
import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/17 13:13
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(OnJavaCondition.class)
public @interface ConditionalOnJava {

    Range range() default Range.EQUAL_OR_NEWER;

    JavaVersion value();

    enum Range {
        EQUAL_OR_NEWER {
            @Override
            boolean isWithin(JavaVersion runningVersion, JavaVersion version) {
                return runningVersion.isEqualOrNewerThan(version);
            }
        },
        OLDER_THAN {
            @Override
            boolean isWithin(JavaVersion runningVersion, JavaVersion version) {
                return runningVersion.isOlderThan(version);
            }
        };

        abstract boolean isWithin(JavaVersion runningVersion, JavaVersion version);
    }
}