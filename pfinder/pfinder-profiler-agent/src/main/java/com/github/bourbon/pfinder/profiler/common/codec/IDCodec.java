package com.github.bourbon.pfinder.profiler.common.codec;

import com.github.bourbon.base.utils.ByteUtils;
import com.github.bourbon.pfinder.profiler.common.exception.PfinderDecodeException;
import com.github.bourbon.pfinder.profiler.common.exception.PfinderEncodeException;
import com.github.bourbon.pfinder.profiler.common.protocol.ID;
import com.github.bourbon.pfinder.profiler.common.util.BytesDecodeReader;
import com.github.bourbon.pfinder.profiler.common.util.BytesEncodingWriter;
import com.github.bourbon.pfinder.profiler.common.util.ChunkCodec;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/3/7 15:23
 */
public class IDCodec {

    public static final ChunkCodec<ID> VAR = new ChunkCodec<ID>() {
        @Override
        public ID readChunk(BytesDecodeReader reader) throws PfinderDecodeException {
            try {
                return IDCodec.decodeVar(reader.getInputStream());
            } catch (IOException e) {
                throw new PfinderDecodeException(e);
            }
        }

        @Override
        public void writeChunk(ID chunk, BytesEncodingWriter writer) throws PfinderEncodeException {
            try {
                IDCodec.encodeVar(chunk, writer.getOutputStream());
            } catch (IOException e) {
                throw new PfinderEncodeException(e);
            }
        }
    };

    public static final ChunkCodec<ID> FIXED = new ChunkCodec<ID>() {
        @Override
        public ID readChunk(BytesDecodeReader reader) throws PfinderDecodeException {
            try {
                return IDCodec.decodeFix(reader.getInputStream());
            } catch (IOException e) {
                throw new PfinderDecodeException(e);
            }
        }

        @Override
        public void writeChunk(ID chunk, BytesEncodingWriter writer) throws PfinderEncodeException {
            try {
                IDCodec.encodeFix(chunk, writer.getOutputStream());
            } catch (IOException e) {
                throw new PfinderEncodeException(e);
            }
        }
    };

    private IDCodec() {
        throw new UnsupportedOperationException();
    }

    public static void encodeVar(ID id, OutputStream os) throws IOException {
        VarByteUtils.writeInt(id.getP1(), os);
        VarByteUtils.writeInt(id.getP2(), os);
        VarByteUtils.writeLong(id.getP3(), os);
    }

    public static ID decodeVar(InputStream is) throws IOException {
        int p1 = VarByteUtils.readInt(is);
        int p2 = VarByteUtils.readInt(is);
        long p3 = VarByteUtils.readLong(is);
        return p1 == 0 && p2 == 0 && p3 == 0L ? ID.NOOP : new ID(p1, p2, p3);
    }

    public static void encodeFix(ID id, OutputStream os) throws IOException {
        ByteUtils.writeInt(id.getP1(), os);
        ByteUtils.writeInt(id.getP2(), os);
        ByteUtils.writeLong(id.getP3(), os);
    }

    public static ID decodeFix(InputStream is) throws IOException {
        int p1 = ByteUtils.readInt(is);
        int p2 = ByteUtils.readInt(is);
        long p3 = ByteUtils.readLong(is);
        return new ID(p1, p2, p3);
    }

    public static ID decodeFix(byte[] bytes) throws IOException {
        int p1 = ByteUtils.readInt(bytes, 0);
        int p2 = ByteUtils.readInt(bytes, 4);
        long p3 = ByteUtils.readLong(bytes, 8);
        return new ID(p1, p2, p3);
    }
}