package com.github.bourbon.base.lang;

import java.util.Comparator;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/20 23:38
 */
public interface Prioritized extends Comparable<Prioritized> {

    Comparator<Object> COMPARATOR = (one, two) -> {
        boolean b1 = one instanceof Prioritized;
        boolean b2 = two instanceof Prioritized;
        if (b1 && !b2) {
            return -1;
        }
        if (b2 && !b1) {
            return 1;
        }
        if (b1) {
            return ((Prioritized) one).compareTo((Prioritized) two);
        }
        return 0;
    };

    int MAX_PRIORITY = Integer.MIN_VALUE;

    int MIN_PRIORITY = Integer.MAX_VALUE;

    int NORMAL_PRIORITY = 0;

    default int getPriority() {
        return NORMAL_PRIORITY;
    }

    @Override
    default int compareTo(Prioritized that) {
        return Integer.compare(getPriority(), that.getPriority());
    }
}