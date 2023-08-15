package com.john.shardingjdbc.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * @author zhangjuwa  <a href="mailto:zhangjuwa@gmail.com">zhangjuwa</a>
 * @date 2023/8/10 21:53
 * @since jdk1.8
 */
@Slf4j
public class JsonUtils {

    private JsonUtils() {
    }

    private static final ObjectMapper mapper;
    private static final TypeFactory typeFactory;

    static {
        mapper = new ObjectMapper();
        mapper.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        mapper.setTimeZone(TimeZone.getDefault());
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        configDateTime();
        configLong();

        typeFactory = mapper.getTypeFactory();
    }

    private static void configDateTime() {
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);

        JavaTimeModule module = new JavaTimeModule();
        module.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        module.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        mapper.registerModule(module);
    }

    private static void configLong() {
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        mapper.registerModule(simpleModule);
    }

    public static <T> String toString(T t) {
        try {
            return mapper.writeValueAsString(t);
        } catch (JsonProcessingException e) {
            log.error("json 转换异常", e);
        }
        return "";
    }

    public static <T> T toObject(String json, Class<T> clz) {
        try {
            return mapper.readValue(json, clz);
        } catch (JsonProcessingException e) {
            log.error("json 转换异常", e);
        }
        return null;
    }

    public static <T> T toObject(String json, TypeReference<T> typeReference) {
        try {
            return mapper.readValue(json, typeReference);
        } catch (JsonProcessingException e) {
            log.error("json 转换异常", e);
        }
        return null;
    }

    public static <T> List<T> toList(String json, Class<T> clz) {
        JavaType javaType = constructCollectionType(List.class, clz);
        try {
            return mapper.readValue(json, javaType);
        } catch (JsonProcessingException e) {
            log.error("json 转换异常", e);
        }
        return Collections.emptyList();
    }

    public static <K, V> Map<K, V> toMap(String json, Class<K> keyClass, Class<V> valueClass) {
        JavaType javaType = constructMapType(keyClass, valueClass);
        try {
            return mapper.readValue(json, javaType);
        } catch (JsonProcessingException e) {
            log.error("json 转换异常", e);
        }
        return Collections.emptyMap();
    }

    private static <T> JavaType constructType(Class<T> inner) {
        return typeFactory.constructType(inner);
    }

    @SuppressWarnings("all")
    private static <T> JavaType constructCollectionType(Class<? extends Collection> genericType, Class<T> inner) {
        return typeFactory.constructCollectionType(genericType, inner);
    }

    private static <K, V> JavaType constructMapType(Class<K> keyClass, Class<V> valueClass) {
        return typeFactory.constructMapType(HashMap.class, keyClass, valueClass);
    }
}
