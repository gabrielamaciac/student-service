package com.learning.student.studentservice.service.util;

import com.learning.student.studentservice.controller.model.StudentDto;
import com.learning.student.studentservice.persistance.model.Student;
import org.modelmapper.ModelMapper;

public class StudentMapper {
    private static final ModelMapper modelMapper = new ModelMapper();

    private StudentMapper() {
    }

    public static StudentDto convertStudentToStudentDto(Student student) {
        return modelMapper.map(student, StudentDto.class);
    }

    public static Student convertStudentDtoToStudent(StudentDto studentDto) {
        return modelMapper.map(studentDto, Student.class);
    }
}
