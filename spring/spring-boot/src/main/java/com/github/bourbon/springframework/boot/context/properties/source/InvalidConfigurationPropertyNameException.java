package com.github.bourbon.springframework.boot.context.properties.source;

import java.util.List;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/25 18:06
 */
public class InvalidConfigurationPropertyNameException extends RuntimeException {

    private static final long serialVersionUID = -8492437309434330064L;

    private final CharSequence name;

    private final List<Character> invalidCharacters;

    public InvalidConfigurationPropertyNameException(CharSequence name, List<Character> invalidCharacters) {
        super("Configuration property name '" + name + "' is not valid");
        this.name = name;
        this.invalidCharacters = invalidCharacters;
    }

    public List<Character> getInvalidCharacters() {
        return invalidCharacters;
    }

    public CharSequence getName() {
        return name;
    }

    public static void throwIfHasInvalidChars(CharSequence name, List<Character> invalidCharacters) {
        if (!invalidCharacters.isEmpty()) {
            throw new InvalidConfigurationPropertyNameException(name, invalidCharacters);
        }
    }
}