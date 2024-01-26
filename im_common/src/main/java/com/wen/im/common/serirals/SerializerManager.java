package com.wen.im.common.serirals;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wenting
 */
public class SerializerManager {

    private static Map<SerializeType, Serializer> serializerMap = new HashMap<>();

    static {
        serializerMap.put(SerializeType.JSON, new JSONSerializer());
    }

    public static Serializer getSerializer(SerializeType type){
        return serializerMap.get(type);
    }

}
