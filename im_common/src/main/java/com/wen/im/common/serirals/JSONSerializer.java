package com.wen.im.common.serirals;

import com.alibaba.fastjson2.JSON;

public class JSONSerializer implements Serializer {
    @Override
    public <T> T deserialize(byte[] body, Class<T> clazz) {
        return JSON.parseObject(body, clazz);
    }

    @Override
    public <T> byte[] serialize(T body) {
        return JSON.toJSONBytes(body);
    }
}
