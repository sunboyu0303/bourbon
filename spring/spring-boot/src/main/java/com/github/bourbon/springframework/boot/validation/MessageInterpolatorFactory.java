package com.github.bourbon.springframework.boot.validation;

import com.github.bourbon.base.utils.ClassUtils;
import com.github.bourbon.base.utils.ObjectUtils;
import com.github.bourbon.base.utils.SetUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.context.MessageSource;

import javax.validation.MessageInterpolator;
import javax.validation.Validation;
import javax.validation.ValidationException;
import java.util.Set;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/27 20:08
 */
public class MessageInterpolatorFactory implements ObjectFactory<MessageInterpolator> {

    private static final Set<String> FALLBACKS = SetUtils.unmodifiableSet(
            SetUtils.newLinkedHashSet("org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator")
    );

    private final MessageSource messageSource;

    public MessageInterpolatorFactory() {
        this(null);
    }

    public MessageInterpolatorFactory(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public MessageInterpolator getObject() throws BeansException {
        MessageInterpolator messageInterpolator = getMessageInterpolator();
        return ObjectUtils.defaultIfNull(messageSource, m -> new MessageSourceMessageInterpolator(m, messageInterpolator), messageInterpolator);
    }

    private MessageInterpolator getMessageInterpolator() {
        try {
            return Validation.byDefaultProvider().configure().getDefaultMessageInterpolator();
        } catch (ValidationException ex) {
            return ObjectUtils.requireNonNull(getFallback(), () -> ex);
        }
    }

    private MessageInterpolator getFallback() {
        for (String fallback : FALLBACKS) {
            try {
                return getFallback(fallback);
            } catch (Exception ex) {
                // Swallow and continue
            }
        }
        return null;
    }

    private MessageInterpolator getFallback(String fallback) {
        return (MessageInterpolator) BeanUtils.instantiateClass(ClassUtils.resolveClassName(fallback));
    }
}