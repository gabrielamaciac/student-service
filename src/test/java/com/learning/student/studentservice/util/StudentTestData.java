package com.learning.student.studentservice.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.student.studentservice.controller.model.Address;
import com.learning.student.studentservice.controller.model.Grade;
import com.learning.student.studentservice.controller.model.Mark;
import com.learning.student.studentservice.controller.model.Student;
import com.learning.student.studentservice.integration.model.AddressMessage;
import com.learning.student.studentservice.integration.model.GradeMessage;
import com.learning.student.studentservice.integration.model.MarkMessage;
import com.learning.student.studentservice.integration.model.StudentMessage;
import com.learning.student.studentservice.persistance.model.AddressEntity;
import com.learning.student.studentservice.persistance.model.GradeEntity;
import com.learning.student.studentservice.persistance.model.MarkEntity;
import com.learning.student.studentservice.persistance.model.StudentDetailsEntity;
import com.learning.student.studentservice.persistance.model.StudentEntity;

import java.util.Collections;
import java.util.UUID;

public class StudentTestData {

    public static final ObjectMapper objectMapper = new ObjectMapper();
    public static final String STUDENT_ID = "4ce2dddb-438a-4305-b7e3-9981fda59355";
    public static final UUID STUDENT_UUID = UUID.fromString(STUDENT_ID);
    public static final String STUDENT_JSON = "{\n" +
            "    \"firstName\": \"Test FirstName\",\n" +
            "    \"lastName\": \"Test LastName\",\n" +
            "    \"cnp\": \"Test CNP\",\n" +
            "    \"dateOfBirth\": \"1987-12-10\",\n" +
            "    \"address\": {\n" +
            "        \"street\": \"Test Street\",\n" +
            "        \"number\": \"Test Number\",\n" +
            "        \"city\": \"Test City\",\n" +
            "        \"country\": \"Test Country\"\n" +
            "    },\n" +
            "    \"grades\": [\n" +
            "        {\n" +
            "            \"subject\": \"Test Subject\",\n" +
            "            \"marks\": [\n" +
            "                {\n" +
            "                    \"dateReceived\": \"2020-03-12\",\n" +
            "                    \"mark\": 10.0\n" +
            "                }\n" +
            "            ]\n" +
            "        }\n" +
            "    ]\n" +
            "}";
    public static final String TEST_FIRST_NAME = "Test FirstName";
    public static final String TEST_LAST_NAME = "Test LastName";
    public static final String TEST_CNP = "Test CNP";
    public static final String DATE_OF_BIRTH = "1987-12-10";
    public static final String TEST_CITY = "Test City";
    public static final String TEST_COUNTRY = "Test Country";
    public static final String TEST_NUMBER = "Test Number";
    public static final String TEST_STREET = "Test Street";
    public static final String TEST_SUBJECT = "Test Subject";
    public static final String DATE_RECEIVED = "2020-03-12";

    public static StudentDetailsEntity getStudentDetailsEntity() {
        return new StudentDetailsEntity(STUDENT_UUID, STUDENT_JSON, false);
    }

    public static StudentEntity getStudentEntity() {
        StudentEntity student = new StudentEntity();
        student.setFirstName(TEST_FIRST_NAME);
        student.setLastName(TEST_LAST_NAME);
        student.setCnp(TEST_CNP);
        student.setDateOfBirth(DATE_OF_BIRTH);
        AddressEntity address = new AddressEntity();
        address.setCity(TEST_CITY);
        address.setCountry(TEST_COUNTRY);
        address.setNumber(TEST_NUMBER);
        address.setStreet(TEST_STREET);
        student.setAddress(address);
        GradeEntity gradeEntity = new GradeEntity();
        gradeEntity.setSubject(TEST_SUBJECT);
        MarkEntity markEntity = new MarkEntity();
        markEntity.setDateReceived(DATE_RECEIVED);
        markEntity.setMark(10.0);
        gradeEntity.setMarks(Collections.singletonList(markEntity));
        student.setGrades(Collections.singletonList(gradeEntity));
        return student;
    }

    public static StudentMessage getStudentMessage() {
        StudentMessage student = new StudentMessage();
        student.setFirstName(TEST_FIRST_NAME);
        student.setLastName(TEST_LAST_NAME);
        student.setCnp(TEST_CNP);
        student.setDateOfBirth(DATE_OF_BIRTH);
        AddressMessage address = new AddressMessage();
        address.setCity(TEST_CITY);
        address.setCountry(TEST_COUNTRY);
        address.setNumber(TEST_NUMBER);
        address.setStreet(TEST_STREET);
        student.setAddress(address);
        GradeMessage gradeMessage = new GradeMessage();
        gradeMessage.setSubject(TEST_SUBJECT);
        MarkMessage markMessage = new MarkMessage();
        markMessage.setDateReceived(DATE_RECEIVED);
        markMessage.setMark(10.0);
        gradeMessage.setMarks(Collections.singletonList(markMessage));
        student.setGrades(Collections.singletonList(gradeMessage));
        return student;
    }

    public static Student getStudent() {
        Student student = new Student();
        student.setId(STUDENT_ID);
        student.setFirstName(TEST_FIRST_NAME);
        student.setLastName(TEST_LAST_NAME);
        student.setCnp(TEST_CNP);
        student.setDateOfBirth(DATE_OF_BIRTH);
        Address address = new Address();
        address.setCity(TEST_CITY);
        address.setCountry(TEST_COUNTRY);
        address.setNumber(TEST_NUMBER);
        address.setStreet(TEST_STREET);
        student.setAddress(address);
        Grade grade = new Grade();
        grade.setSubject(TEST_SUBJECT);
        Mark mark = new Mark();
        mark.setDateReceived(DATE_RECEIVED);
        mark.setMark(10.0);
        grade.setMarks(Collections.singletonList(mark));
        student.setGrades(Collections.singletonList(grade));
        student.setValid(true);
        return student;
    }

    public static Student getStudentFromJson() {
        try {
            Student student = objectMapper.readValue(STUDENT_JSON, Student.class);
            student.setId(STUDENT_ID);
            return student;
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
