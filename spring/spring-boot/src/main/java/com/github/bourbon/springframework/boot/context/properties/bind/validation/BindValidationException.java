package com.github.bourbon.springframework.boot.context.properties.bind.validation;

import com.github.bourbon.base.lang.Assert;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/29 17:55
 */
public class BindValidationException extends RuntimeException {

    private static final long serialVersionUID = -727045274362462491L;

    private final transient ValidationErrors validationErrors;

    BindValidationException(ValidationErrors validationErrors) {
        super(getMessage(validationErrors));
        Assert.notNull(validationErrors, "ValidationErrors must not be null");
        this.validationErrors = validationErrors;
    }

    public ValidationErrors getValidationErrors() {
        return validationErrors;
    }

    private static String getMessage(ValidationErrors errors) {
        StringBuilder message = new StringBuilder("Binding validation errors");
        if (errors != null) {
            message.append(" on ").append(errors.getName());
            errors.getAllErrors().forEach(e -> message.append(String.format("%n   - %s", e)));
        }
        return message.toString();
    }
}