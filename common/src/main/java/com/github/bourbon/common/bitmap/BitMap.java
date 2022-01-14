package com.github.bourbon.common.bitmap;

import org.roaringbitmap.RoaringBitmap;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/21 16:12
 */
public class BitMap {

    private RoaringBitmap roaringBitmap;

    private BitMap() {
        roaringBitmap = new RoaringBitmap();
    }

    private BitMap(int... dat) {
        roaringBitmap = RoaringBitmap.bitmapOf(dat);
    }

    public void add(int... dat) {
        roaringBitmap.add(dat);
    }

    public boolean contains(int i) {
        return roaringBitmap.contains(i);
    }

    public long size() {
        return roaringBitmap.getLongCardinality();
    }

    public void clear() {
        roaringBitmap.clear();
    }

    public boolean isEmpty() {
        return roaringBitmap.isEmpty();
    }

    public void remove(int i) {
        roaringBitmap.remove(i);
    }

    public static BitMap of() {
        return new BitMap();
    }

    public static BitMap of(int... dat) {
        return new BitMap(dat);
    }
}