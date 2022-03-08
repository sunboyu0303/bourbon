package com.github.bourbon.pfinder.profiler.common.util;

import com.github.bourbon.base.utils.ByteUtils;
import com.github.bourbon.base.utils.MapUtils;
import com.github.bourbon.pfinder.profiler.common.exception.PfinderDecodeException;
import com.github.bourbon.pfinder.profiler.common.exception.PfinderEncodeException;
import com.github.bourbon.pfinder.profiler.common.protocol.Pair;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/3/7 17:20
 */
public class BytesDecodeReader {
    private static final byte[] EMPTY_BYTES = new byte[0];
    private final InputStream is;

    public BytesDecodeReader(InputStream is) {
        this.is = is;
    }

    public BytesDecodeReader(byte[] bytes) {
        this(new ByteArrayInputStream(bytes));
    }

    public InputStream getInputStream() {
        return this.is;
    }

    public short readInt16() throws PfinderDecodeException {
        try {
            return VarByteUtils.readShort(this.is);
        } catch (IOException e) {
            throw new PfinderDecodeException(e);
        }
    }

    public byte[] readInt16Bytes() throws PfinderDecodeException {
        try {
            return VarByteUtils.readShortBytes(this.is);
        } catch (IOException e) {
            throw new PfinderDecodeException(e);
        }
    }

    public int readInt32() throws PfinderDecodeException {
        try {
            return VarByteUtils.readInt(this.is);
        } catch (IOException e) {
            throw new PfinderDecodeException(e);
        }
    }

    public byte[] readInt32Bytes() throws PfinderDecodeException {
        try {
            return VarByteUtils.readIntBytes(this.is);
        } catch (IOException e) {
            throw new PfinderDecodeException(e);
        }
    }

    public long readInt64() throws PfinderDecodeException {
        try {
            return VarByteUtils.readLong(this.is);
        } catch (IOException e) {
            throw new PfinderDecodeException(e);
        }
    }

    public byte[] readInt64Bytes() throws PfinderDecodeException {
        try {
            return VarByteUtils.readLongBytes(this.is);
        } catch (IOException e) {
            throw new PfinderDecodeException(e);
        }
    }

    public short readFixed16() throws PfinderDecodeException {
        try {
            return ByteUtils.readShort(this.is);
        } catch (IOException e) {
            throw new PfinderDecodeException(e);
        }
    }

    public int readFixed32() throws PfinderDecodeException {
        try {
            return ByteUtils.readInt(this.is);
        } catch (IOException e) {
            throw new PfinderDecodeException(e);
        }
    }

    public long readFixed64() throws PfinderDecodeException {
        try {
            return ByteUtils.readLong(this.is);
        } catch (IOException e) {
            throw new PfinderDecodeException(e);
        }
    }

    public float readFloat() throws PfinderDecodeException {
        try {
            return ByteUtils.readFloat(this.is);
        } catch (IOException e) {
            throw new PfinderDecodeException(e);
        }
    }

    public byte[] readFloatBytes() throws PfinderDecodeException {
        return this.readBytes(4);
    }

    public double readDouble() throws PfinderDecodeException {
        try {
            return ByteUtils.readDouble(this.is);
        } catch (IOException e) {
            throw new PfinderDecodeException(e);
        }
    }

    public byte[] readDoubleBytes() throws PfinderDecodeException {
        return this.readBytes(8);
    }

    public byte[] readField() throws PfinderDecodeException {
        return this.readBytes(this.readInt32());
    }

    public <T> T readField(FieldDecoder<T> decoder) throws PfinderDecodeException {
        return decoder.decode(this.readField());
    }

    public <T> T readChunk(ChunkDecoder<T> decoder) throws PfinderDecodeException {
        return decoder.readChunk(this);
    }

    public <T> List<T> readList(ChunkDecoder<T> decoder) throws PfinderDecodeException {
        int size = this.readInt32();
        if (size == 0) {
            return Collections.emptyList();
        }
        List<T> list = new ArrayList<>(size);
        for (int i = 0; i < size; ++i) {
            list.add(this.readChunk(decoder));
        }
        return list;
    }

    public byte[] readListBytes(ChunkDecoder<byte[]> itemBytesReader) throws PfinderDecodeException {
        BytesEncodingWriter writer = new BytesEncodingWriter();
        int size = this.readInt32();
        try {
            writer.writeInt32(size);
            if (size > 0) {
                for (int i = 0; i < size; ++i) {
                    writer.writeAllBytes((byte[]) this.readChunk(itemBytesReader));
                }
            }
        } catch (PfinderEncodeException e) {
            throw new PfinderDecodeException(e);
        }
        return writer.toByteArray();
    }

    public <T> Map<T, T> readMap(ChunkDecoder<T> decoder) throws PfinderDecodeException {
        return this.readMap(decoder, decoder);
    }

    public <K, V> Map<K, V> readMap(ChunkDecoder<K> keyDecoder, ChunkDecoder<V> valueDecoder) throws PfinderDecodeException {
        int size = this.readInt32();
        if (size == 0) {
            return Collections.emptyMap();
        }
        Map<K, V> result = MapUtils.newHashMap(size);
        for (int i = 0; i < size; ++i) {
            result.put(this.readChunk(keyDecoder), this.readChunk(valueDecoder));
        }
        return result;
    }

    public byte[] readMapBytes(ChunkDecoder<byte[]> decoder) throws PfinderDecodeException {
        return this.readMapBytes(decoder, decoder);
    }

    public byte[] readMapBytes(ChunkDecoder<byte[]> keyDecoder, ChunkDecoder<byte[]> valueDecoder) throws PfinderDecodeException {
        BytesEncodingWriter writer = new BytesEncodingWriter();
        int size = this.readInt32();
        try {
            writer.writeInt32(size);
            if (size > 0) {
                for (int i = 0; i < size; ++i) {
                    writer.writeAllBytes(this.readChunk(keyDecoder));
                    writer.writeAllBytes(this.readChunk(valueDecoder));
                }
            }
        } catch (PfinderEncodeException e) {
            throw new PfinderDecodeException(e);
        }
        return writer.toByteArray();
    }

    public <T> List<Pair<T, T>> readPairs(ChunkDecoder<T> decoder) throws PfinderDecodeException {
        return this.readPairs(decoder, decoder);
    }

    public <K, V> List<Pair<K, V>> readPairs(ChunkDecoder<K> keyDecoder, ChunkDecoder<V> valueDecoder) throws PfinderDecodeException {
        int size = this.readInt32();
        if (size == 0) {
            return Collections.emptyList();
        }
        List<Pair<K, V>> result = new ArrayList<>(size);
        for (int i = 0; i < size; ++i) {
            result.add(Pair.of(this.readChunk(keyDecoder), this.readChunk(valueDecoder)));
        }
        return result;
    }

    public byte readByte() throws PfinderDecodeException {
        try {
            return (byte) this.is.read();
        } catch (IOException e) {
            throw new PfinderDecodeException(e);
        }
    }

    public byte[] readAllBytes() throws PfinderDecodeException {
        try {
            return this.readBytes(this.is.available());
        } catch (IOException e) {
            throw new PfinderDecodeException(e);
        }
    }

    public byte[] readBytes(int size) throws PfinderDecodeException {
        if (size < 0) {
            throw new PfinderDecodeException("invalid read size. size=" + size);
        }
        if (size == 0) {
            return EMPTY_BYTES;
        }
        try {
            byte[] bytes = new byte[size];
            this.is.read(bytes);
            return bytes;
        } catch (IOException e) {
            throw new PfinderDecodeException(e);
        }
    }

    public boolean readable(int limit) throws PfinderDecodeException {
        try {
            return this.is.available() >= limit;
        } catch (IOException e) {
            throw new PfinderDecodeException(e);
        }
    }

    public boolean readable() throws PfinderDecodeException {
        return this.readable(1);
    }
}