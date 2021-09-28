package com.learning.student.studentservice.facade;

import com.learning.student.studentservice.controller.model.Student;
import com.learning.student.studentservice.persistance.model.StudentEntity;
import com.learning.student.studentservice.service.StudentService;
import com.learning.student.studentservice.util.StudentTestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test {@link StudentFacade}
 */
class StudentFacadeTest {
    private StudentService studentService;
    private StudentFacade studentFacade;

    private Student expectedStudent = StudentTestData.getStudent();
    private StudentEntity expectedStudentEntity = StudentTestData.getStudentEntity(UUID.randomUUID().toString());

    @BeforeEach
    void setUp() {
        studentService = mock(StudentService.class);
        studentFacade = new StudentFacadeImpl(studentService);
    }

    @Test
    void createStudentReturnsValidStudent() {
        // Given
        when(studentService.create(expectedStudentEntity)).thenReturn(expectedStudent);

        // When
        Student actualStudent = studentFacade.create(expectedStudentEntity);

        // Then
        assertEquals(expectedStudent.getId(), actualStudent.getId());
        assertEquals(expectedStudent.getCnp(), actualStudent.getCnp());
        assertEquals(expectedStudent.getFirstName(), actualStudent.getFirstName());
    }


    @Test
    void getStudentByIdReturnsValidStudent() {
        // Given
        when(studentService.getById(StudentTestData.STUDENT_ID)).thenReturn(expectedStudent);

        // When
        Student actualStudent = studentFacade.getById(StudentTestData.STUDENT_ID);

        // Then
        assertEquals(expectedStudent.getId(), actualStudent.getId());
        assertEquals(expectedStudent.getCnp(), actualStudent.getCnp());
        assertEquals(expectedStudent.getFirstName(), actualStudent.getFirstName());
    }

    @Test
    void getAllStudentsReturnsValidStudentList() {
        // Given
        List<Student> expectedStudentList = Arrays.asList(expectedStudent);
        when(studentService.getAll(0, 10)).thenReturn(expectedStudentList);

        // When
        List<Student> actualList = studentFacade.getAll(0, 10);

        // Then
        assertEquals(expectedStudentList.get(0).getId(), actualList.get(0).getId());
        assertEquals(expectedStudentList.get(0).getCnp(), actualList.get(0).getCnp());
        assertEquals(expectedStudentList.get(0).getFirstName(), actualList.get(0).getFirstName());
    }

    @Test
    void updateStudentIsSuccessful() {
        studentFacade.update(StudentTestData.STUDENT_ID, expectedStudentEntity);

        // verify method delegation only
        verify(studentService).update(StudentTestData.STUDENT_ID, expectedStudentEntity);
    }

    @Test
    void deleteStudentIsSuccessful() {
        studentFacade.delete(StudentTestData.STUDENT_ID);

        // verify method delegation only
        verify(studentService).delete(StudentTestData.STUDENT_ID);
    }
}
