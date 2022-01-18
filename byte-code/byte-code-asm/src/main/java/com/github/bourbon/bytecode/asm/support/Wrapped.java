package com.github.bourbon.bytecode.asm.support;

import com.github.bourbon.base.constant.CharConstants;
import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.utils.CollectionUtils;
import com.github.bourbon.bytecode.core.constant.ByteCodeConstants;
import com.github.bourbon.bytecode.core.domain.MethodDescription;
import com.github.bourbon.bytecode.core.utils.ClassUtils;
import org.objectweb.asm.*;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static com.github.bourbon.base.constant.StringConstants.*;
import static com.github.bourbon.bytecode.asm.constant.ASMConstants.*;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/10 18:31
 */
public class Wrapped<T> implements Opcodes {

    private static final AtomicLong ATOMIC_LONG = new AtomicLong();

    private final T t;
    private final Class<?> clazz;
    private final String clazzSimpleName;
    private final String clazzName;
    private final String clazzInternalName;
    private final String clazzDescriptor;
    private final String proxySimpleName;
    private final String proxyClassName;
    private final String proxyInternalName;
    private final String proxyDescriptor;
    private final String monitorInternalName;
    private final String startMethodName;
    private final String endMethodName;

    private final T object;

    public Wrapped(T t) throws Exception {
        this(t, DefaultMonitor.class, START, END);
    }

    public Wrapped(T t, Class<?> monitorClazz, String startMethodName, String endMethodName) throws Exception {
        long idx = ATOMIC_LONG.getAndIncrement();
        this.t = t;
        this.clazz = t.getClass();
        this.clazzSimpleName = clazz.getSimpleName();
        this.clazzName = clazz.getName();
        this.clazzInternalName = Type.getInternalName(clazz);
        this.clazzDescriptor = Type.getDescriptor(clazz);
        this.proxySimpleName = clazz.getSimpleName() + ByteCodeConstants.PROXY_CLASS + DOLLAR + idx;
        this.proxyClassName = clazz.getName() + ByteCodeConstants.PROXY + DOLLAR + idx;
        this.proxyInternalName = proxyClassName.replace(CharConstants.DOT, CharConstants.SLASH);
        this.proxyDescriptor = L + proxyInternalName + SEMICOLON;
        this.monitorInternalName = Type.getInternalName(monitorClazz);
        this.startMethodName = startMethodName;
        this.endMethodName = endMethodName;
        this.object = init();
    }

    @SuppressWarnings("unchecked")
    private T init() throws Exception {
        ClassWriter cw = new ClassWriter(0);
        // 定义对象头；版本号、修饰符、全类名、签名、父类、实现的接口
        cw.visit(V1_8, ACC_PUBLIC | ACC_SUPER, proxyInternalName, null, clazzInternalName, null);
        // 源文件名
        cw.visitSource(proxySimpleName, null);
        // 设置属性
        cw.visitField(ACC_PRIVATE, TARGET, clazzDescriptor, null, null).visitEnd();
        // 设置构造方法
        init(cw);
        // 设置 setTarget 方法
        setTarget(cw);
        // 设置 getTarget 方法
        getTarget(cw);

        for (Method method : clazz.getMethods()) {
            if (ClassUtils.exceptMethod(method.getName()) && (Modifier.isPublic(method.getModifiers()) || Modifier.isProtected(method.getModifiers()))) {
                invoke(cw, getMethodInfo(method));
            }
        }

        byte[] bytes = cw.toByteArray();
        ClassUtils.outputClazz(proxyInternalName, bytes);
        // 加载 AsmHelloWorld
        Class<?> defineClass = ClassUtils.defineClassFromClassFile(proxyClassName, bytes);
        Object obj = defineClass.newInstance();
        obj.getClass().getMethod(SET_TARGET, clazz).invoke(obj, t);
        return (T) obj;
    }

    private void init(ClassWriter cw) {
        // 定义构造方法
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, INIT, LEFT_RIGHT_PARENTHESES_V, null, null);
        mv.visitCode();

        Label label0 = new Label();
        mv.visitLabel(label0);

        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(INVOKESPECIAL, clazzInternalName, INIT, LEFT_RIGHT_PARENTHESES_V, false);

        Label label1 = new Label();
        mv.visitLabel(label1);
        mv.visitInsn(RETURN);

        Label label2 = new Label();
        mv.visitLabel(label2);
        mv.visitLocalVariable(THIS, proxyDescriptor, null, label0, label2, 0);

        mv.visitMaxs(1, 1);
        mv.visitEnd();
    }

    private void setTarget(ClassWriter cw) {
        // setTarget 方法
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, SET_TARGET, LEFT_PARENTHESES + clazzDescriptor + RIGHT_PARENTHESES_V, null, null);
        mv.visitParameter(TARGET, 0);
        mv.visitCode();

        Label label0 = new Label();
        mv.visitLabel(label0);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 1);
        mv.visitFieldInsn(PUTFIELD, proxyInternalName, TARGET, clazzDescriptor);

        Label label1 = new Label();
        mv.visitLabel(label1);
        mv.visitInsn(RETURN);

        Label label2 = new Label();
        mv.visitLabel(label2);
        mv.visitLocalVariable(THIS, proxyDescriptor, null, label0, label2, 0);
        mv.visitLocalVariable(TARGET, clazzDescriptor, null, label0, label2, 1);

        mv.visitMaxs(2, 2);
        mv.visitEnd();
    }

    private void getTarget(ClassWriter cw) {
        // getTarget 方法
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, GET_TARGET, LEFT_RIGHT_PARENTHESES + clazzDescriptor, null, null);
        mv.visitCode();

        Label label0 = new Label();
        mv.visitLabel(label0);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitFieldInsn(GETFIELD, proxyInternalName, TARGET, clazzDescriptor);

        mv.visitInsn(ARETURN);

        Label label1 = new Label();
        mv.visitLabel(label1);
        mv.visitLocalVariable(THIS, proxyDescriptor, null, label0, label1, 0);

        mv.visitMaxs(1, 1);
        mv.visitEnd();
    }

    private void invoke(ClassWriter cw, MethodDescription info) {
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, info.getMethodName(), info.getMethodType(), null, info.getExceptionTypeInternalNameList().toArray(StringConstants.EMPTY_STRING_ARRAY));
        mv.visitCode();
        Label label0 = new Label();
        Label label1 = new Label();
        Label label2 = new Label();
        String exceptionType = null;
        List<String> exceptionTypeInternalNameList = info.getExceptionTypeInternalNameList();
        if (!CollectionUtils.isEmpty(exceptionTypeInternalNameList)) {
            exceptionType = exceptionTypeInternalNameList.get(0);
        }
        mv.visitTryCatchBlock(label0, label1, label2, exceptionType);
        mv.visitMethodInsn(INVOKESTATIC, monitorInternalName, startMethodName, LEFT_RIGHT_PARENTHESES_V, false);
        mv.visitLabel(label0);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitFieldInsn(GETFIELD, proxyInternalName, TARGET, clazzDescriptor);

        List<String> parameterTypeDescList = info.getParameterTypeList();

        int idx = 1;
        for (String parameterTypeDesc : parameterTypeDescList) {
            switch (parameterTypeDesc) {
                case "Z":
                case "B":
                case "C":
                case "S":
                case "I":
                    mv.visitVarInsn(ILOAD, idx++);
                    break;
                case "J":
                    mv.visitVarInsn(LLOAD, idx);
                    idx++;
                    idx++;
                    break;
                case "F":
                    mv.visitVarInsn(FLOAD, idx++);
                    break;
                case "D":
                    mv.visitVarInsn(DLOAD, idx);
                    idx++;
                    idx++;
                    break;
                default:
                    mv.visitVarInsn(ALOAD, idx++);
                    break;
            }
        }

        int maxStack = idx;

        mv.visitMethodInsn(INVOKEVIRTUAL, clazzInternalName, info.getMethodName(), info.getMethodType(), false);

        switch (info.getReturnType()) {
            case "V":
                break;
            case "Z":
            case "B":
            case "C":
            case "S":
            case "I":
                mv.visitVarInsn(ISTORE, idx++);
                break;
            case "J":
                mv.visitVarInsn(LSTORE, idx);
                idx++;
                idx++;
                break;
            case "F":
                mv.visitVarInsn(FSTORE, idx++);
                break;
            case "D":
                mv.visitVarInsn(DSTORE, idx);
                idx++;
                idx++;
                break;
            default:
                mv.visitVarInsn(ASTORE, idx++);
                break;
        }

        int maxLocals = idx + 1;

        mv.visitLabel(label1);
        mv.visitMethodInsn(INVOKESTATIC, monitorInternalName, endMethodName, LEFT_RIGHT_PARENTHESES_V, false);
        Label label3 = new Label();
        mv.visitJumpInsn(GOTO, label3);
        mv.visitLabel(label2);
        mv.visitFrame(Opcodes.F_SAME1, 0, null, 1, new Object[]{"java/lang/Throwable"});

        mv.visitVarInsn(ASTORE, idx);

        mv.visitMethodInsn(INVOKESTATIC, monitorInternalName, endMethodName, LEFT_RIGHT_PARENTHESES_V, false);
        mv.visitVarInsn(ALOAD, idx);

        mv.visitInsn(ATHROW);
        mv.visitLabel(label3);

        switch (info.getReturnType()) {
            case "V":
                mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
                mv.visitInsn(RETURN);
                break;
            case "Z":
            case "B":
            case "C":
            case "S":
            case "I":
                idx--;
                mv.visitFrame(Opcodes.F_APPEND, 1, new Object[]{INTEGER}, 0, null);
                mv.visitVarInsn(ILOAD, idx);
                mv.visitInsn(IRETURN);
                break;
            case "J":
                idx--;
                idx--;
                mv.visitFrame(Opcodes.F_APPEND, 1, new Object[]{LONG}, 0, null);
                mv.visitVarInsn(LLOAD, idx);
                mv.visitInsn(LRETURN);
                break;
            case "F":
                idx--;
                mv.visitFrame(Opcodes.F_APPEND, 1, new Object[]{FLOAT}, 0, null);
                mv.visitVarInsn(FLOAD, idx);
                mv.visitInsn(FRETURN);
                break;
            case "D":
                idx--;
                idx--;
                mv.visitFrame(Opcodes.F_APPEND, 1, new Object[]{DOUBLE}, 0, null);
                mv.visitVarInsn(DLOAD, idx);
                mv.visitInsn(DRETURN);
                break;
            default:
                idx--;
                mv.visitFrame(Opcodes.F_APPEND, 1, new Object[]{info.getReturnTypeInternalName()}, 0, null);
                mv.visitVarInsn(ALOAD, idx);
                mv.visitInsn(ARETURN);
                break;
        }

        mv.visitMaxs(maxStack, maxLocals);
        mv.visitEnd();
    }

    private MethodDescription getMethodInfo(Method method) {
        MethodDescription description = new MethodDescription()
                .setClazz(clazz)
                .setFullClassName(clazzName)
                .setSimpleClassName(clazzSimpleName)
                .setMethod(method)
                .setMethodName(method.getName())
                .setMethodType(Type.getType(method).toString());

        List<String> parameterTypeDescList = ListUtils.newArrayList();
        List<String> parameterTypeInternalNameList = ListUtils.newArrayList();
        for (Class<?> parameterType : method.getParameterTypes()) {
            parameterTypeDescList.add(Type.getType(parameterType).toString());
            parameterTypeInternalNameList.add(Type.getInternalName(parameterType));
        }
        description.setParameterTypeList(parameterTypeDescList)
                .setParameterTypeInternalNameList(parameterTypeInternalNameList)
                .setReturnTypeInternalName(Type.getInternalName(method.getReturnType()))
                .setReturnType(Type.getType(method.getReturnType()).toString());
        List<String> exceptionTypeDescList = ListUtils.newArrayList();
        List<String> exceptionTypeInternalNameList = ListUtils.newArrayList();
        for (Class<?> exceptionType : method.getExceptionTypes()) {
            exceptionTypeDescList.add(Type.getType(exceptionType).toString());
            exceptionTypeInternalNameList.add(Type.getInternalName(exceptionType));
        }
        description.setExceptionTypeList(exceptionTypeDescList).setExceptionTypeInternalNameList(exceptionTypeInternalNameList);
        return description;
    }

    public T getObject() {
        return object;
    }
}