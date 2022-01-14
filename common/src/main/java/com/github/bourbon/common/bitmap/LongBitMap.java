package com.github.bourbon.common.bitmap;

import org.roaringbitmap.longlong.Roaring64Bitmap;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/21 16:13
 */
public class LongBitMap {

    private Roaring64Bitmap roaring64Bitmap;

    private LongBitMap() {
        roaring64Bitmap = new Roaring64Bitmap();
    }

    private LongBitMap(long... dat) {
        roaring64Bitmap = Roaring64Bitmap.bitmapOf(dat);
    }

    public void add(long... dat) {
        roaring64Bitmap.add(dat);
    }

    public boolean contains(long l) {
        return roaring64Bitmap.contains(l);
    }

    public long size() {
        return roaring64Bitmap.getLongCardinality();
    }

    public void clear() {
        roaring64Bitmap.clear();
    }

    public boolean isEmpty() {
        return roaring64Bitmap.isEmpty();
    }

    public void remove(long l) {
        roaring64Bitmap.removeLong(l);
    }

    public static LongBitMap of() {
        return new LongBitMap();
    }

    public static LongBitMap of(long... dat) {
        return new LongBitMap(dat);
    }
}