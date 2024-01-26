package com.wen.im.common.serirals;

/**
 * @author wenting
 */
public interface Serializer {
    public <T> T deserialize(byte[] body, Class<T> clazz);
    public <T> byte[] serialize(T body);
}
