package com.github.bourbon.tracer.core.constants;

import com.github.bourbon.base.constant.StringConstants;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/30 17:52
 */
public final class SofaTracerConstants {

    public static final String SAMPLER_TYPE_TAG_KEY = "sampler.type";

    public static final String SAMPLER_PARAM_TAG_KEY = "sampler.param";

    public static final String DEFAULT_UTF8_ENCODING = "UTF-8";

    public static final Charset DEFAULT_UTF8_CHARSET = StandardCharsets.UTF_8;

    public static final String RPC_2_JVM_DIGEST_LOG_NAME = "rpc-2-jvm-digest.log";

    public static final String MS = "ms";

    public static final String BYTE = StringConstants.B;

    public static final int MAX_LAYER = 100;

    public static final String BIZ_ERROR = "biz_error";

    public static final String LOAD_TEST_TAG = "mark";

    public static final String LOAD_TEST_VALUE = StringConstants.T;

    public static final String NON_LOAD_TEST_VALUE = StringConstants.F;

    public static final String RESULT_CODE_SUCCESS = "00";

    public static final String RESULT_CODE_TIME_OUT = "03";

    public static final String RESULT_CODE_ERROR = "99";

    public static final String RESULT_SUCCESS = "success";

    public static final String RESULT_FAILED = "failed";

    public static final String DIGEST_FLAG_SUCCESS = StringConstants.Y;

    public static final String DIGEST_FLAG_FAILS = StringConstants.N;

    public static final String STAT_FLAG_SUCCESS = DIGEST_FLAG_SUCCESS;

    public static final String STAT_FLAG_FAILS = DIGEST_FLAG_FAILS;

    public static final String SPACE_ID = "sofa-tracer";

    private SofaTracerConstants() {
    }
}