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
import com.learning.student.studentservice.integration.model.search.OperationType;
import com.learning.student.studentservice.integration.model.search.SearchPayload;
import com.learning.student.studentservice.integration.model.search.StudentSearch;
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
        AddressEntity address = new AddressEntity(TEST_CITY, TEST_COUNTRY, TEST_NUMBER, TEST_STREET);
        MarkEntity mark = new MarkEntity(DATE_RECEIVED, 10.0);
        GradeEntity grade = new GradeEntity(TEST_SUBJECT, Collections.singletonList(mark));
        return new StudentEntity(TEST_FIRST_NAME, TEST_LAST_NAME, TEST_CNP, DATE_OF_BIRTH, address, Collections.singletonList(grade));
    }

    public static StudentMessage getStudentMessage() {
        AddressMessage address = new AddressMessage(TEST_CITY, TEST_COUNTRY, TEST_NUMBER, TEST_STREET);
        MarkMessage mark = new MarkMessage(DATE_RECEIVED, 10.0);
        GradeMessage grade = new GradeMessage(TEST_SUBJECT, Collections.singletonList(mark));
        return new StudentMessage(TEST_FIRST_NAME, TEST_LAST_NAME, TEST_CNP, DATE_OF_BIRTH, address, Collections.singletonList(grade));
    }

    public static Student getStudent() {
        Address address = new Address(TEST_CITY, TEST_COUNTRY, TEST_NUMBER, TEST_STREET);
        Mark mark = new Mark(DATE_RECEIVED, 10.0);
        Grade grade = new Grade(TEST_SUBJECT, Collections.singletonList(mark));
        return new Student(STUDENT_ID, TEST_FIRST_NAME, TEST_LAST_NAME, TEST_CNP, DATE_OF_BIRTH, address, Collections.singletonList(grade), true);
    }

    public static SearchPayload getSearchPayload() {
        StudentSearch studentSearch = new StudentSearch(STUDENT_ID, TEST_FIRST_NAME, TEST_LAST_NAME, TEST_CNP, true);
        return new SearchPayload(OperationType.CREATE, studentSearch);
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
