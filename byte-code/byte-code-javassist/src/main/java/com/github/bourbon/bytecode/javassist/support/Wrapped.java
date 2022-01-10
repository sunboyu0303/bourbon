package com.github.bourbon.bytecode.javassist.support;

import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.bytecode.core.utils.ClassUtils;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.bytecode.LocalVariableAttribute;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiConsumer;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/11 00:10
 */
public class Wrapped<T> {

    private static final AtomicLong ATOMIC_LONG = new AtomicLong();
    private final T object;

    public Wrapped(Class<T> clazz) throws Exception {
        this(clazz, (c, m) -> {
            try {
                CtMethod ctMethod = c.getDeclaredMethod(m.getName());
                LocalVariableAttribute attr = (LocalVariableAttribute) ctMethod.getMethodInfo().getCodeAttribute().getAttribute(LocalVariableAttribute.tag);
                CtClass[] parameterTypes = ctMethod.getParameterTypes();

                List<String> parameterNameList = new ArrayList<>();
                List<String> parameterTypeList = new ArrayList<>();
                for (int i = 0; i < parameterTypes.length; i++) {
                    parameterNameList.add(attr.variableName((i + 1)));
                    parameterTypeList.add(parameterTypes[i].getName());
                }

                int idx = DefaultMonitor.generateMethodId(c.getName(), ctMethod.getName(), parameterNameList, parameterTypeList, ctMethod.getReturnType().getName());

                ctMethod.addLocalVariable("startTime", CtClass.longType);
                ctMethod.insertBefore("{ startTime = com.github.bourbon.base.lang.Clock.currentTimeMillis(); }");
                ctMethod.insertAfter("{ com.github.bourbon.bytecode.javassist.support.DefaultMonitor.point(" + idx + ", startTime); }", false);
                ctMethod.addCatch("{ com.github.bourbon.bytecode.javassist.support.DefaultMonitor.point(" + idx + ", $e); throw $e; }", ClassPool.getDefault().get("java.lang.Exception"));
            } catch (Exception e) {
                throw new IllegalArgumentException(e);
            }
        });
    }

    @SuppressWarnings("unchecked")
    public Wrapped(Class<T> clazz, BiConsumer<CtClass, Method> biConsumer) throws Exception {
        CtClass ctClass = ClassPool.getDefault().get(clazz.getName());
        for (Method method : clazz.getDeclaredMethods()) {
            if (ClassUtils.exceptMethod(method.getName()) && Modifier.isPublic(method.getModifiers())) {
                biConsumer.accept(ctClass, method);
            }
        }
        ctClass.setName(ctClass.getName() + StringConstants.DOLLAR + ATOMIC_LONG.getAndIncrement());
        ctClass.writeFile(getClass().getResource(StringConstants.SLASH).getPath());
        this.object = (T) ClassUtils.defineClassFromClassFile(ctClass.getName(), ctClass.toBytecode()).newInstance();
    }

    public T getObject() {
        return object;
    }
}