package com.github.bourbon.springframework.boot.validation;

import com.github.bourbon.base.constant.CharConstants;
import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.utils.ObjectUtils;
import com.github.bourbon.base.utils.SetUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import javax.validation.MessageInterpolator;
import java.util.Locale;
import java.util.Set;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/29 16:18
 */
class MessageSourceMessageInterpolator implements MessageInterpolator {

    private static final char PREFIX = CharConstants.LEFT_BRACES;

    private static final char SUFFIX = CharConstants.RIGHT_BRACES;

    private static final char ESCAPE = CharConstants.BACKSLASH;

    private final MessageSource messageSource;

    private final MessageInterpolator messageInterpolator;

    MessageSourceMessageInterpolator(MessageSource messageSource, MessageInterpolator messageInterpolator) {
        this.messageSource = messageSource;
        this.messageInterpolator = messageInterpolator;
    }

    @Override
    public String interpolate(String messageTemplate, Context context) {
        return interpolate(messageTemplate, context, LocaleContextHolder.getLocale());
    }

    @Override
    public String interpolate(String messageTemplate, Context context, Locale locale) {
        return messageInterpolator.interpolate(replaceParameters(messageTemplate, locale), context, locale);
    }

    private String replaceParameters(String message, Locale locale) {
        return replaceParameters(message, locale, SetUtils.newLinkedHashSet(4));
    }

    private String replaceParameters(String message, Locale locale, Set<String> visitedParameters) {
        StringBuilder buf = new StringBuilder(message);
        int parentheses = 0;
        int startIndex = -1;
        int endIndex = -1;
        for (int i = 0; i < buf.length(); i++) {
            if (buf.charAt(i) == ESCAPE) {
                i++;
            } else if (buf.charAt(i) == PREFIX) {
                if (startIndex == -1) {
                    startIndex = i;
                }
                parentheses++;
            } else if (buf.charAt(i) == SUFFIX) {
                if (parentheses > 0) {
                    parentheses--;
                }
                endIndex = i;
            }
            if (parentheses == 0 && startIndex < endIndex) {
                String parameter = buf.substring(startIndex + 1, endIndex);
                if (!visitedParameters.add(parameter)) {
                    throw new IllegalArgumentException("Circular reference " + StringConstants.SINGLE_QUOTE_LEFT_BRACES +
                            String.join(StringConstants.SPACE_HYPHEN_GREATER_THAN_SPACE, visitedParameters) +
                            StringConstants.SPACE_HYPHEN_GREATER_THAN_SPACE + parameter + StringConstants.RIGHT_BRACES_SINGLE_QUOTE);
                }
                String value = replaceParameter(parameter, locale, visitedParameters);
                if (value != null) {
                    buf.replace(startIndex, endIndex + 1, value);
                    i = startIndex + value.length() - 1;
                }
                visitedParameters.remove(parameter);
                startIndex = -1;
                endIndex = -1;
            }
        }
        return buf.toString();
    }

    private String replaceParameter(String parameter, Locale locale, Set<String> visitedParameters) {
        return ObjectUtils.defaultIfNullElseFunction(messageSource.getMessage(replaceParameters(parameter, locale, visitedParameters), null, null, locale), v -> replaceParameters(v, locale, visitedParameters));
    }
}