package com.github.bourbon.pfinder.profiler.common.protocol;

import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.pfinder.profiler.common.codec.IDCodec;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/3/7 15:16
 */
public class ID {

    public static final ID NOOP = new ID(0, 0, 0L) {
        @Override
        public String toReadableString() {
            return "0.0.0";
        }
    };

    @Tag(1)
    private int p1;
    @Tag(2)
    private int p2;
    @Tag(3)
    private long p3;

    public static ID formReadableString(String readableString) {
        String[] tmp = readableString.split("\\.");
        if (tmp.length != 3) {
            throw new IllegalArgumentException("invalid ID -> " + readableString);
        }
        return new ID(Integer.parseInt(tmp[0]), Integer.parseInt(tmp[1]), Long.parseLong(tmp[2]));
    }

    public ID() {
    }

    public ID(int p1, int p2, long p3) {
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
    }

    public int getP1() {
        return this.p1;
    }

    public ID setP1(int p1) {
        this.p1 = p1;
        return this;
    }

    public int getP2() {
        return this.p2;
    }

    public ID setP2(int p2) {
        this.p2 = p2;
        return this;
    }

    public long getP3() {
        return this.p3;
    }

    public ID setP3(long p3) {
        this.p3 = p3;
        return this;
    }

    public String toReadableString() {
        return this.p1 + StringConstants.DOT + this.p2 + StringConstants.DOT + this.p3;
    }

    public byte[] toVarBytes() {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            IDCodec.encodeVar(this, os);
        } catch (IOException e) {
            throw new RuntimeException("ID encode error", e);
        }
        return os.toByteArray();
    }

    public byte[] toFixBytes() {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            IDCodec.encodeFix(this, os);
        } catch (IOException e) {
            throw new RuntimeException("ID encode error", e);
        }
        return os.toByteArray();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ID)) {
            return false;
        }
        ID target = (ID) obj;
        return this.p1 == target.getP1() && this.p2 == target.getP2() && this.p3 == target.getP3();
    }

    @Override
    public String toString() {
        return this.toReadableString();
    }
}