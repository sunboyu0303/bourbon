package com.github.bourbon.bytecode.core.domain;

import com.alibaba.fastjson.JSON;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/10 17:47
 */
public class MethodDescription {
    private Class<?> clazz;
    private String fullClassName;
    private String simpleClassName;
    private Method method;
    private String methodName;
    private String methodType;
    private List<String> parameterNameList;
    private List<String> parameterTypeList;
    private List<String> parameterTypeInternalNameList;
    private String returnType;
    private String returnTypeInternalName;
    private List<String> exceptionTypeList;
    private List<String> exceptionTypeInternalNameList;

    public Class<?> getClazz() {
        return clazz;
    }

    public MethodDescription setClazz(Class<?> clazz) {
        this.clazz = clazz;
        return this;
    }

    public String getFullClassName() {
        return fullClassName;
    }

    public MethodDescription setFullClassName(String fullClassName) {
        this.fullClassName = fullClassName;
        return this;
    }

    public String getSimpleClassName() {
        return simpleClassName;
    }

    public MethodDescription setSimpleClassName(String simpleClassName) {
        this.simpleClassName = simpleClassName;
        return this;
    }

    public Method getMethod() {
        return method;
    }

    public MethodDescription setMethod(Method method) {
        this.method = method;
        return this;
    }

    public String getMethodName() {
        return methodName;
    }

    public MethodDescription setMethodName(String methodName) {
        this.methodName = methodName;
        return this;
    }

    public String getMethodType() {
        return methodType;
    }

    public MethodDescription setMethodType(String methodType) {
        this.methodType = methodType;
        return this;
    }

    public List<String> getParameterNameList() {
        return parameterNameList;
    }

    public MethodDescription setParameterNameList(List<String> parameterNameList) {
        this.parameterNameList = parameterNameList;
        return this;
    }

    public List<String> getParameterTypeList() {
        return parameterTypeList;
    }

    public MethodDescription setParameterTypeList(List<String> parameterTypeList) {
        this.parameterTypeList = parameterTypeList;
        return this;
    }

    public List<String> getParameterTypeInternalNameList() {
        return parameterTypeInternalNameList;
    }

    public MethodDescription setParameterTypeInternalNameList(List<String> parameterTypeInternalNameList) {
        this.parameterTypeInternalNameList = parameterTypeInternalNameList;
        return this;
    }

    public String getReturnType() {
        return returnType;
    }

    public MethodDescription setReturnType(String returnType) {
        this.returnType = returnType;
        return this;
    }

    public String getReturnTypeInternalName() {
        return returnTypeInternalName;
    }

    public MethodDescription setReturnTypeInternalName(String returnTypeInternalName) {
        this.returnTypeInternalName = returnTypeInternalName;
        return this;
    }

    public List<String> getExceptionTypeList() {
        return exceptionTypeList;
    }

    public MethodDescription setExceptionTypeList(List<String> exceptionTypeList) {
        this.exceptionTypeList = exceptionTypeList;
        return this;
    }

    public List<String> getExceptionTypeInternalNameList() {
        return exceptionTypeInternalNameList;
    }

    public MethodDescription setExceptionTypeInternalNameList(List<String> exceptionTypeInternalNameList) {
        this.exceptionTypeInternalNameList = exceptionTypeInternalNameList;
        return this;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}