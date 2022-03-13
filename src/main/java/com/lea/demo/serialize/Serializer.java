package com.lea.demo.serialize;

public interface Serializer {

    Serializer DEFAULT = new JSONSerializer();

    /**
     *      序列化算法
     * @return
     */
    byte getSerializerAlgorithm();

    //序列化
    byte[] serialize(Object obj);

    <T> T deserialize(Class<T> clazz, byte[] bytes);
}
