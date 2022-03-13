package com.lea.demo.serialize;

import com.google.gson.Gson;

import java.nio.charset.StandardCharsets;

public class JSONSerializer implements Serializer {

    private static Gson gson = new Gson();

    @Override
    public byte getSerializerAlgorithm() {
        return SerializerAlgorithm.JSON;
    }

    @Override
    public byte[] serialize(Object obj) {
        return gson.toJson(obj).getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public <T> T deserialize(Class<T> clazz, byte[] bytes) {
        return gson.fromJson(new String(bytes), clazz);
    }
}
