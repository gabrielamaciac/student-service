package com.learning.student.studentservice.service.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.student.studentservice.controller.model.StudentDto;
import com.learning.student.studentservice.persistance.model.Student;
import com.learning.student.studentservice.persistance.model.StudentEntity;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;

@Slf4j
public class StudentMapper {
    private static final ModelMapper modelMapper = new ModelMapper();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private StudentMapper() {
    }

    public static StudentDto convertStudentToStudentDto(Student student) {
        return modelMapper.map(student, StudentDto.class);
    }

    public static Student convertStudentDtoToStudent(StudentDto studentDto) {
        return modelMapper.map(studentDto, Student.class);
    }

    public static Student convertJsonToStudent(StudentEntity studentEntity) {
        try {
            return objectMapper.readValue(studentEntity.getStudentJson(), Student.class);
        } catch (JsonProcessingException e) {
            log.info("Error converting json to Student object: " + e.getMessage());
        }
        return null;
    }

    public static String convertStudentToJson(Student student) {
        try {
            return objectMapper.writeValueAsString(student);
        } catch (JsonProcessingException e) {
            log.info("Error converting Student object to json: " + e.getMessage());
        }
        return null;
    }
}
