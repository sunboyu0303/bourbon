package com.github.bourbon.base.utils;

import com.github.bourbon.base.lang.Holder;

import java.io.*;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/28 17:29
 */
public final class StreamUtils {

    public static void io(InputStream in, OutputStream out) throws IOException {
        io(in, out, -1);
    }

    public static void io(InputStream in, OutputStream out, int bufferSize) throws IOException {
        if (bufferSize == -1) {
            bufferSize = 8192;
        }
        byte[] buffer = new byte[bufferSize];
        int amount;
        while ((amount = in.read(buffer)) >= 0) {
            out.write(buffer, 0, amount);
        }
    }

    public static void io(Reader in, Writer out) throws IOException {
        io(in, out, -1);
    }

    public static void io(Reader in, Writer out, int bufferSize) throws IOException {
        if (bufferSize == -1) {
            bufferSize = 4096;
        }
        char[] buffer = new char[bufferSize];
        int amount;
        while ((amount = in.read(buffer)) >= 0) {
            out.write(buffer, 0, amount);
        }
    }

    public static OutputStream synchronizedOutputStream(OutputStream out) {
        return new SynchronizedOutputStream(out);
    }

    public static String readText(InputStream in) throws IOException {
        return readText(in, null);
    }

    public static String readText(InputStream in, String encoding) throws IOException {
        return readText(in, encoding, -1);
    }

    public static String readText(InputStream in, String encoding, int bufferSize) throws IOException {
        return readText(encoding == null ? new InputStreamReader(in) : new InputStreamReader(in, encoding), bufferSize);
    }

    public static String readText(Reader reader) throws IOException {
        return readText(reader, -1);
    }

    public static String readText(Reader reader, int bufferSize) throws IOException {
        StringWriter writer = new StringWriter();
        io(reader, writer, bufferSize);
        return writer.toString();
    }

    private static class SynchronizedOutputStream extends OutputStream {
        private Holder<OutputStream> holder;

        SynchronizedOutputStream(OutputStream out) {
            this.holder = Holder.of(out);
        }

        @Override
        public void write(int datum) throws IOException {
            holder.lock();
            try {
                this.holder.get().write(datum);
            } finally {
                holder.unlock();
            }
        }

        @Override
        public void write(byte[] data) throws IOException {
            holder.lock();
            try {
                this.holder.get().write(data);
            } finally {
                holder.unlock();
            }
        }

        @Override
        public void write(byte[] data, int offset, int length) throws IOException {
            holder.lock();
            try {
                this.holder.get().write(data, offset, length);
            } finally {
                holder.unlock();
            }
        }

        @Override
        public void flush() throws IOException {
            holder.lock();
            try {
                this.holder.get().flush();
            } finally {
                holder.unlock();
            }
        }

        @Override
        public void close() throws IOException {
            holder.lock();
            try {
                this.holder.get().close();
            } finally {
                holder.unlock();
            }
        }
    }

    private StreamUtils() {
    }
}