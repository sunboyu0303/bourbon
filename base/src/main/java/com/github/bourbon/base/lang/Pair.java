package com.github.bourbon.base.lang;

import com.github.bourbon.base.utils.ObjectUtils;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/13 23:46
 */
public class Pair<R1, R2> {
    public final R1 r1;
    public final R2 r2;

    public static <C1, C2> Pair<C1, C2> of(C1 c1, C2 c2) {
        return new Pair<>(c1, c2);
    }

    private Pair(R1 r1, R2 r2) {
        this.r1 = r1;
        this.r2 = r2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Pair)) {
            return false;
        }
        Pair<?, ?> p = (Pair) o;
        return ObjectUtils.equals(r1, p.r1) && ObjectUtils.equals(r2, p.r2);
    }

    @Override
    public int hashCode() {
        return ObjectUtils.hash(r1, r2);
    }
}