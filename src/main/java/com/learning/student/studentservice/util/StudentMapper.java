package com.learning.student.studentservice.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;

@Slf4j
public class StudentMapper {
    private static final ModelMapper modelMapper = new ModelMapper();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private StudentMapper() {
    }

    public static <T, O> T map(final O object, final Class<T> type) {
        return modelMapper.map(object, type);
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
