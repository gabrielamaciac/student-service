package com.learning.student.studentservice.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GenericMapper {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private GenericMapper() {
    }

    public static <T> String writeValue(final T object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("Error converting {} object to json: ", object.getClass(), e);
        }
        return null;
    }

    public static <T> T readValue(final String json, final Class<T> type) {
        try {
            return objectMapper.readValue(json, type);
        } catch (JsonProcessingException e) {
            log.error("Error converting json to {}.", type, e);
        }
        return null;
    }
}
