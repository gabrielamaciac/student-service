package com.learning.student.studentservice.service.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.student.studentservice.controller.model.Student;
import com.learning.student.studentservice.integration.model.StudentMessage;
import com.learning.student.studentservice.persistance.model.StudentDetailsEntity;
import com.learning.student.studentservice.persistance.model.StudentEntity;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;

@Slf4j
public class StudentMapper {
    private static final ModelMapper modelMapper = new ModelMapper();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private StudentMapper() {
    }

    public static StudentEntity convertStudentToStudentEntity(Student student) {
        return modelMapper.map(student, StudentEntity.class);
    }

    public static StudentEntity convertStudentMessageToStudentEntity(StudentMessage studentMessage) {
        return modelMapper.map(studentMessage, StudentEntity.class);
    }

    public static String convertStudentEntityToJson(StudentEntity studentEntity) {
        try {
            return objectMapper.writeValueAsString(studentEntity);
        } catch (JsonProcessingException e) {
            log.info("Error converting StudentEntity object to json: " + e.getMessage());
        }
        return null;
    }

    public static Student convertJsonToStudent(StudentDetailsEntity studentDetailsEntity) {
        try {
            return objectMapper.readValue(studentDetailsEntity.getStudentJson(), Student.class);
        } catch (JsonProcessingException e) {
            log.info("Error converting json to StudentEntity object: " + e.getMessage());
        }
        return null;
    }
}
