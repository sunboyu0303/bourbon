package com.github.bourbon.tracer.core.registry;

import com.github.bourbon.base.utils.ByteUtils;
import com.github.bourbon.tracer.core.appender.self.SelfLog;
import com.github.bourbon.tracer.core.constants.SofaTracerConstants;
import com.github.bourbon.tracer.core.context.span.SofaTracerSpanContext;
import io.opentracing.propagation.Format;

import java.nio.ByteBuffer;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/2/9 16:59
 */
public class BinaryFormater implements RegistryExtractorInjector<ByteBuffer> {

    private static final byte[] FORMATER_KEY_HEAD_BYTES = FORMATER_KEY_HEAD.getBytes(SofaTracerConstants.DEFAULT_UTF8_CHARSET);

    @Override
    public Format<ByteBuffer> getFormatType() {
        return Format.Builtin.BINARY;
    }

    @Override
    public SofaTracerSpanContext extract(ByteBuffer carrier) {
        if (carrier == null || carrier.array().length < FORMATER_KEY_HEAD_BYTES.length) {
            return SofaTracerSpanContext.rootStart();
        }
        // head
        byte[] formaterKeyHeadBytes = FORMATER_KEY_HEAD_BYTES;
        int index = ByteUtils.indexOf(carrier.array(), formaterKeyHeadBytes);
        if (index < 0) {
            return SofaTracerSpanContext.rootStart();
        }
        try {
            // (UTF-8) Put the head from 0
            carrier.position(index + formaterKeyHeadBytes.length);
            // value byte arrays
            byte[] contextDataBytes = new byte[carrier.getInt()];
            carrier.get(contextDataBytes);
            return SofaTracerSpanContext.deserializeFromString(new String(contextDataBytes, SofaTracerConstants.DEFAULT_UTF8_CHARSET));
        } catch (Exception e) {
            SelfLog.error("com.github.bourbon.tracer.core.registry.BinaryFormater.extract Error.Recover by root start", e);
            return SofaTracerSpanContext.rootStart();
        }
    }

    @Override
    public void inject(SofaTracerSpanContext spanContext, ByteBuffer carrier) {
        if (carrier == null || spanContext == null) {
            return;
        }
        // head
        carrier.put(FORMATER_KEY_HEAD_BYTES);
        byte[] value = spanContext.serializeSpanContext().getBytes(SofaTracerConstants.DEFAULT_UTF8_CHARSET);
        // length
        carrier.putInt(value.length);
        // data
        carrier.put(value);
    }
}