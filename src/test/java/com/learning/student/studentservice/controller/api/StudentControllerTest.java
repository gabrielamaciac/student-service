package com.learning.student.studentservice.controller.api;

import com.learning.student.studentservice.controller.model.Student;
import com.learning.student.studentservice.facade.StudentFacade;
import com.learning.student.studentservice.persistance.model.StudentEntity;
import com.learning.student.studentservice.util.StudentTestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class StudentControllerTest {
    private final Student expectedStudent = StudentTestData.getStudent();

    private StudentFacade studentFacade;
    private ModelMapper modelMapper;
    private StudentApi studentController;

    @BeforeEach
    void setUp() {
        studentFacade = mock(StudentFacade.class);
        modelMapper = new ModelMapper();
        studentController = new StudentController(studentFacade, modelMapper);
    }

    @Test
    void createStudentReturns201StatusCodeAndCorrectStudent() {
        // given
        given(studentFacade.create(any(StudentEntity.class))).willReturn(expectedStudent);

        // when
        ResponseEntity<Student> response = studentController.create(expectedStudent);

        // then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(expectedStudent.getCnp(), response.getBody().getCnp());
    }

    @Test
    void getStudentByIdReturns200StatusCodeAndCorrectStudent() {
        // given
        given(studentFacade.getById(StudentTestData.STUDENT_ID)).willReturn(expectedStudent);

        // when
        ResponseEntity<Student> response = studentController.getById(StudentTestData.STUDENT_ID);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedStudent.getCnp(), response.getBody().getCnp());
    }

    @Test
    void getAllStudentsReturns200StatusCodeAndCorrectStudentList() {
        // given
        List<Student> expectedStudentList = Collections.singletonList(expectedStudent);
        given(studentFacade.getAll(0, 10)).willReturn(expectedStudentList);

        // when
        ResponseEntity<CollectionModel<Student>> response = studentController.getAll(0, 10);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedStudentList.size(), response.getBody().getContent().size());
        assertEquals(expectedStudentList.get(0).getCnp(),
                response.getBody().getContent().stream().findFirst().get().getCnp());
    }

    @Test
    void updateStudentReturns204StatusCode() {
        // when
        ResponseEntity<Void> response = studentController.updateById(StudentTestData.STUDENT_ID, expectedStudent);

        // then
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(studentFacade).update(any(String.class), any(StudentEntity.class));
    }

    @Test
    void deleteStudentReturns204StatusCode() {
        // when
        ResponseEntity<Void> response = studentController.deleteById(StudentTestData.STUDENT_ID);

        // then
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(studentFacade).delete(StudentTestData.STUDENT_ID);
    }
}
