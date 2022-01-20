package com.github.bourbon.base.utils;

import java.lang.reflect.Member;
import java.lang.reflect.Modifier;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/12 23:34
 */
public interface MemberUtils {

    static boolean isPublic(Member member) {
        return member != null && Modifier.isPublic(member.getModifiers());
    }

    static boolean isPrivate(Member member) {
        return member != null && Modifier.isPrivate(member.getModifiers());
    }

    static boolean isProtected(Member member) {
        return member != null && Modifier.isProtected(member.getModifiers());
    }

    static boolean isStatic(Member member) {
        return member != null && Modifier.isStatic(member.getModifiers());
    }

    static boolean isFinal(Member member) {
        return member != null && Modifier.isFinal(member.getModifiers());
    }

    static boolean isSynchronized(Member member) {
        return member != null && Modifier.isSynchronized(member.getModifiers());
    }

    static boolean isVolatile(Member member) {
        return member != null && Modifier.isVolatile(member.getModifiers());
    }

    static boolean isTransient(Member member) {
        return member != null && Modifier.isTransient(member.getModifiers());
    }

    static boolean isNative(Member member) {
        return member != null && Modifier.isNative(member.getModifiers());
    }

    static boolean isInterface(Member member) {
        return member != null && Modifier.isInterface(member.getModifiers());
    }

    static boolean isAbstract(Member member) {
        return member != null && Modifier.isAbstract(member.getModifiers());
    }
}