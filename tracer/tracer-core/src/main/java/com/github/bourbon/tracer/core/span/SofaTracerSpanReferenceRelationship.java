package com.github.bourbon.tracer.core.span;

import com.github.bourbon.base.lang.Assert;
import com.github.bourbon.tracer.core.context.span.SofaTracerSpanContext;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/24 15:36
 */
public class SofaTracerSpanReferenceRelationship {

    private final SofaTracerSpanContext sofaTracerSpanContext;

    private final String referenceType;

    public SofaTracerSpanReferenceRelationship(SofaTracerSpanContext sofaTracerSpanContext, String referenceType) {
        Assert.notNull(sofaTracerSpanContext, "SofaTracerSpanContext can't be null in SofaTracerSpanReferenceRelationship");
        Assert.notBlank(referenceType, "ReferenceType can't be null");
        this.sofaTracerSpanContext = sofaTracerSpanContext;
        this.referenceType = referenceType;
    }

    public SofaTracerSpanContext getSofaTracerSpanContext() {
        return sofaTracerSpanContext;
    }

    public String getReferenceType() {
        return referenceType;
    }
}