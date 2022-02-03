package com.gm.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public class JacksonUtil {

    public static String toJSON(Object object) {
        ObjectMapper mapper = Mapper.INSTANCE.getObjectMapper();
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("toJSON Exception: {}", e.getMessage());
            return "";
        }
    }

    public static <T> T toObject(String json, Class<T> clazz) {
        if (json == null) {
            return null;
        }
        ObjectMapper mapper = Mapper.INSTANCE.getObjectMapper();
        try {
            return mapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            log.warn(e.getMessage());
            return null;
        }
    }

    public enum Mapper {
        INSTANCE;
        private final ObjectMapper mapper = new ObjectMapper();

        Mapper() {
        }

        public ObjectMapper getObjectMapper() {
            JavaTimeModule javaTimeModule = new JavaTimeModule();
            javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern("HH:mm:ss")));
            mapper.registerModule(javaTimeModule);
            return mapper;
        }
    }

}
