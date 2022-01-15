package com.github.bourbon.springframework.boot.context.properties.bind.validation;

import com.github.bourbon.base.utils.ObjectUtils;
import com.github.bourbon.springframework.boot.origin.Origin;
import com.github.bourbon.springframework.boot.origin.OriginProvider;
import org.springframework.validation.FieldError;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/29 17:51
 */
final class OriginTrackedFieldError extends FieldError implements OriginProvider {

    private static final long serialVersionUID = -5198665910541816999L;

    private final transient Origin origin;

    private OriginTrackedFieldError(FieldError fieldError, Origin origin) {
        super(fieldError.getObjectName(), fieldError.getField(), fieldError.getRejectedValue(),
                fieldError.isBindingFailure(), fieldError.getCodes(), fieldError.getArguments(),
                fieldError.getDefaultMessage());
        this.origin = origin;
    }

    @Override
    public Origin getOrigin() {
        return origin;
    }

    @Override
    public String toString() {
        return ObjectUtils.defaultSupplierIfNull(origin, o -> super.toString() + "; origin " + o, super::toString);
    }

    static FieldError of(FieldError fieldError, Origin origin) {
        if (fieldError == null || origin == null) {
            return fieldError;
        }
        return new OriginTrackedFieldError(fieldError, origin);
    }
}