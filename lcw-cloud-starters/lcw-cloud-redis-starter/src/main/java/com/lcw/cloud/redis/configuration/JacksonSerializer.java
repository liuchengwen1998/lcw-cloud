package com.lcw.cloud.redis.configuration;

import org.redisson.client.codec.BaseCodec;
import org.redisson.client.protocol.Decoder;
import org.redisson.client.protocol.Encoder;

/**
 * jackson序列化
 *
 * @author ：yzhang
 * @date ：2022/4/18 17:50
 * @version:
 */
public class JacksonSerializer extends BaseCodec {

    @Override
    public Decoder<Object> getValueDecoder() {
        return null;
    }

    @Override
    public Encoder getValueEncoder() {
        return null;
    }
}
