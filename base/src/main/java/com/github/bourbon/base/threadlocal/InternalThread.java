package com.github.bourbon.base.threadlocal;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/22 10:49
 */
public class InternalThread extends Thread {

    private InternalThreadLocalMap threadLocalMap;

    public InternalThread() {
    }

    public InternalThread(Runnable r) {
        super(r);
    }

    public InternalThread(ThreadGroup g, Runnable r) {
        super(g, r);
    }

    public InternalThread(String name) {
        super(name);
    }

    public InternalThread(ThreadGroup g, String name) {
        super(g, name);
    }

    public InternalThread(Runnable r, String name) {
        super(r, name);
    }

    public InternalThread(ThreadGroup g, Runnable r, String name) {
        super(g, r, name);
    }

    public InternalThread(ThreadGroup g, Runnable r, String name, long stackSize) {
        super(g, r, name, stackSize);
    }

    public InternalThreadLocalMap getThreadLocalMap() {
        return threadLocalMap;
    }

    public void setThreadLocalMap(InternalThreadLocalMap threadLocalMap) {
        this.threadLocalMap = threadLocalMap;
    }
}