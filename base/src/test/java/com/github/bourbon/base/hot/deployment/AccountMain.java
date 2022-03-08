package com.github.bourbon.base.hot.deployment;

import com.github.bourbon.base.utils.TimeUnitUtils;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/3/9 21:55
 */
public class AccountMain {

    @Test
    public void test() throws Exception {
        while (true) {
            ClassLoader loader = new ClassLoader() {
                @Override
                public Class<?> loadClass(String name) throws ClassNotFoundException {
                    try {
                        String fileName = name.substring(name.lastIndexOf(".") + 1) + ".class";
                        InputStream is = getClass().getResourceAsStream(fileName);
                        if (is == null) {
                            return super.loadClass(name);
                        }
                        byte[] b = new byte[is.available()];
                        is.read(b);
                        return defineClass(name, b, 0, b.length);
                    } catch (IOException e) {
                        throw new ClassNotFoundException(name);
                    }
                }
            };
            Class clazz = loader.loadClass(Account.class.getName());
            clazz.getMethod("operation").invoke(clazz.newInstance());
            TimeUnitUtils.sleepMilliSeconds(20000);
        }
    }
}