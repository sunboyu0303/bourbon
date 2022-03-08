package com.github.bourbon.pfinder.profiler.common.util;

import com.github.bourbon.base.utils.ByteUtils;
import com.github.bourbon.pfinder.profiler.common.exception.PfinderEncodeException;
import com.github.bourbon.pfinder.profiler.common.protocol.Pair;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/3/7 17:39
 */
public class BytesEncodingWriter {
    private final ByteArrayOutputStream os;

    public BytesEncodingWriter() {
        this.os = new ByteArrayOutputStream();
    }

    public BytesEncodingWriter(int size) {
        this.os = new ByteArrayOutputStream(size);
    }

    public BytesEncodingWriter(ByteArrayOutputStream os) {
        this.os = os;
    }

    public OutputStream getOutputStream() {
        return this.os;
    }

    public byte[] toByteArray() {
        return this.os.toByteArray();
    }

    public void writeInt16(short value) throws PfinderEncodeException {
        try {
            VarByteUtils.writeShort(value, this.os);
        } catch (IOException var3) {
            throw new PfinderEncodeException(var3);
        }
    }

    public void writeFixed16(short value) throws PfinderEncodeException {
        try {
            ByteUtils.writeShort(value, this.os);
        } catch (IOException var3) {
            throw new PfinderEncodeException(var3);
        }
    }

    public void writeInt32(int value) throws PfinderEncodeException {
        try {
            VarByteUtils.writeInt(value, this.os);
        } catch (IOException var3) {
            throw new PfinderEncodeException(var3);
        }
    }

    public void writeFixed32(int value) throws PfinderEncodeException {
        try {
            ByteUtils.writeInt(value, this.os);
        } catch (IOException var3) {
            throw new PfinderEncodeException(var3);
        }
    }

    public void writeInt64(long value) throws PfinderEncodeException {
        try {
            VarByteUtils.writeLong(value, this.os);
        } catch (IOException var4) {
            throw new PfinderEncodeException(var4);
        }
    }

    public void writeFixed64(long value) throws PfinderEncodeException {
        try {
            ByteUtils.writeLong(value, this.os);
        } catch (IOException var4) {
            throw new PfinderEncodeException(var4);
        }
    }

    public void writeFloat(float value) throws PfinderEncodeException {
        try {
            ByteUtils.writeFloat(value, this.os);
        } catch (IOException var3) {
            throw new PfinderEncodeException(var3);
        }
    }

    public void writeDouble(double value) throws PfinderEncodeException {
        try {
            ByteUtils.writeDouble(value, this.os);
        } catch (IOException var4) {
            throw new PfinderEncodeException(var4);
        }
    }

    public <T> void writeField(T value, FieldEncoder<T> encoder) throws PfinderEncodeException {
        this.writeField(encoder.encode(value));
    }

    public void writeField(byte[] bytes) throws PfinderEncodeException {
        if (bytes != null && bytes.length != 0) {
            this.writeInt32(bytes.length);
            this.writeAllBytes(bytes);
        } else {
            this.writeInt32(0);
        }
    }

    public <T> void writeChunk(T value, ChunkEncoder<T> encoder) throws PfinderEncodeException {
        encoder.writeChunk(value, this);
    }

    public <T> void writeList(List<T> list, ChunkEncoder<T> encoder) throws PfinderEncodeException {
        if (list != null && !list.isEmpty()) {
            this.writeInt32(list.size());
            for (T item : list) {
                this.writeChunk(item, encoder);
            }
        } else {
            this.writeInt32(0);
        }
    }

    public <T> void writeMap(Map<T, T> map, ChunkEncoder<T> encoder) throws PfinderEncodeException {
        this.writeMap(map, encoder, encoder);
    }

    public <K, V> void writeMap(Map<K, V> map, ChunkEncoder<K> keyEncoder, ChunkEncoder<V> valueEncoder) throws PfinderEncodeException {
        if (map != null && !map.isEmpty()) {
            this.writeInt32(map.size());
            for (Map.Entry<K, V> entry : map.entrySet()) {
                this.writeChunk(entry.getKey(), keyEncoder);
                this.writeChunk(entry.getValue(), valueEncoder);
            }
        } else {
            this.writeInt32(0);
        }
    }

    public <T> void writePairs(List<Pair<T, T>> pairs, ChunkEncoder<T> encoder) throws PfinderEncodeException {
        this.writePairs(pairs, encoder, encoder);
    }

    public <K, V> void writePairs(List<Pair<K, V>> pairs, ChunkEncoder<K> keyEncoder, ChunkEncoder<V> valueEncoder) throws PfinderEncodeException {
        if (pairs != null && !pairs.isEmpty()) {
            this.writeInt32(pairs.size());
            for (Pair<K, V> pair : pairs) {
                this.writeChunk(pair.getKey(), keyEncoder);
                this.writeChunk(pair.getValue(), valueEncoder);
            }
        } else {
            this.writeInt32(0);
        }
    }

    public <T> void writeKvCollection(Collection<? extends Map.Entry<T, T>> kvCollection, ChunkEncoder<T> encoder) throws PfinderEncodeException {
        this.writeKvCollection(kvCollection, encoder, encoder);
    }

    public <K, V> void writeKvCollection(Collection<? extends Map.Entry<K, V>> kvCollection, ChunkEncoder<K> keyEncoder, ChunkEncoder<V> valueEncoder) throws PfinderEncodeException {
        if (kvCollection != null && !kvCollection.isEmpty()) {
            this.writeInt32(kvCollection.size());
            for (Map.Entry<K, V> entry : kvCollection) {
                this.writeChunk(entry.getKey(), keyEncoder);
                this.writeChunk(entry.getValue(), valueEncoder);
            }
        } else {
            this.writeInt32(0);
        }
    }

    public void writeAllBytes(byte[] bytes) throws PfinderEncodeException {
        if (bytes != null && bytes.length != 0) {
            try {
                this.os.write(bytes);
            } catch (IOException var3) {
                throw new PfinderEncodeException(var3);
            }
        }
    }

    public void writeByte(byte aByte) {
        this.os.write(aByte);
    }
}