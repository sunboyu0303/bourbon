package com.github.bourbon.springframework.boot.origin;

import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.base.utils.ListUtils;
import com.github.bourbon.base.utils.ObjectUtils;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/19 15:11
 */
public interface Origin {

    default Origin getParent() {
        return null;
    }

    static Origin from(Object source) {
        return BooleanUtils.defaultSupplierIfAssignableFrom(source, Origin.class, Origin.class::cast, () -> {
            Origin origin = null;
            if (source instanceof OriginProvider) {
                origin = ((OriginProvider) source).getOrigin();
            }
            if (origin == null && source instanceof Throwable) {
                return from(((Throwable) source).getCause());
            }
            return origin;
        });
    }

    static List<Origin> parentsFrom(Object source) {
        return ObjectUtils.defaultSupplierIfNullElseFunction(from(source), origin -> {
            Set<Origin> parents = new LinkedHashSet<>();
            origin = origin.getParent();
            while (origin != null && !parents.contains(origin)) {
                parents.add(origin);
                origin = origin.getParent();
            }
            return ListUtils.unmodifiableList(ListUtils.newArrayList(parents));
        }, Collections::emptyList);
    }
}