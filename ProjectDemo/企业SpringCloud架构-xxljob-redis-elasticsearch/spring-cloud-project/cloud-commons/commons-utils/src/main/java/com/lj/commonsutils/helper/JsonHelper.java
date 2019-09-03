package com.lj.commonsutils.helper;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

/**
 * json 帮助API
 */
public class JsonHelper {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * toJSONString
     * @param object
     * @return
     */
    public static final String toJSONString(Object object) {
        return toJSONString(object, new SerializerFeature[0]);
    }

    /***
     * toJSONString
     * @param object
     * @param features
     * @return
     */
    public static final String toJSONString(Object object, SerializerFeature... features) {
        SerializeWriter out = new SerializeWriter();
        try {
            JSONSerializer serializer = new JSONSerializer(out);
            for (com.alibaba.fastjson.serializer.SerializerFeature feature : features) {
                serializer.config(feature, true);
            }
            serializer.write(object);
            return out.toString();
        } finally {
            out.close();
        }
    }

    /**
     * toObject
     * @param obj
     * @param clazz
     * @param <E>
     * @return
     * @throws RuntimeException
     */
    public static <E> E toObject(Object obj, Class<E> clazz) throws RuntimeException {
        try {
            if (obj instanceof String) {
                if (clazz == String.class) {
                    return (E) obj;
                } else {
                    return MAPPER.readValue((String) obj, clazz);
                }
            } else if (obj instanceof JsonNode) {
                return MAPPER.readValue(obj.toString(), clazz);
            } else {
                return MAPPER.readValue(MAPPER.writeValueAsString(obj), clazz);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * toList
     * @param obj
     * @param clazz
     * @param <E>
     * @return
     * @throws RuntimeException
     */
    public static <E> List<E> toList(Object obj, Class<E> clazz) throws RuntimeException {
        JavaType type = MAPPER.getTypeFactory().constructParametricType(List.class, clazz);
        try {
            if (obj instanceof String) {
                return MAPPER.readValue((String) obj, type);
            } else if (obj instanceof JsonNode) {
                return MAPPER.readValue(obj.toString(), type);
            } else {
                return MAPPER.readValue(MAPPER.writeValueAsString(obj), type);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * toJson
     * @param obj
     * @return
     * @throws RuntimeException
     */
    public static JsonNode toJson(Object obj) throws RuntimeException {
        if (obj instanceof String) {
            try {
                return MAPPER.readTree((String) obj);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            return MAPPER.valueToTree(obj);
        }
    }

}
