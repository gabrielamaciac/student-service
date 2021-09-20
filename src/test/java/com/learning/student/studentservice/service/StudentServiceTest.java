package com.learning.student.studentservice.service;

import com.learning.student.studentservice.controller.model.Student;
import com.learning.student.studentservice.integration.queue.SearchServiceSender;
import com.learning.student.studentservice.integration.queue.ValidationServiceSender;
import com.learning.student.studentservice.persistance.model.StudentDetailsEntity;
import com.learning.student.studentservice.persistance.model.StudentEntity;
import com.learning.student.studentservice.persistance.repository.StudentRepository;
import com.learning.student.studentservice.service.impl.StudentServiceImpl;
import com.learning.student.studentservice.util.StudentTestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test for {@link StudentService}
 */
class StudentServiceTest {

    private final StudentDetailsEntity studentDetailsEntity = StudentTestData.getStudentDetailsEntity();
    private final StudentEntity studentEntity = StudentTestData.getStudentEntity();
    private final Student studentFromJson = StudentTestData.getStudentFromJson();

    private StudentRepository studentRepository;
    private StudentService studentService;
    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        studentRepository = mock(StudentRepository.class);
        ValidationServiceSender validationServiceSender = mock(ValidationServiceSender.class);
        SearchServiceSender searchServiceSender = mock(SearchServiceSender.class);
        modelMapper = new ModelMapper();
        studentService = new StudentServiceImpl(studentRepository, validationServiceSender, searchServiceSender, modelMapper);
    }

    @Test
    void getStudentByIdIsSuccessful() {
        // Given
        when(studentRepository.findById(StudentTestData.STUDENT_UUID))
                .thenReturn(Optional.of(studentDetailsEntity));

        // When
        Student actualStudent = studentService.getById(StudentTestData.STUDENT_ID);

        // Then
        assertEquals(studentFromJson.getFirstName(), actualStudent.getFirstName());
        assertEquals(studentFromJson.getAddress().getCity(), actualStudent.getAddress().getCity());
        assertEquals(studentFromJson.getGrades().get(0).getMarks().get(0).getMark(),
                actualStudent.getGrades().get(0).getMarks().get(0).getMark());

    }

    @Test
    void getStudentByIdThrowsException() {
        // Given
        when(studentRepository.findById(StudentTestData.STUDENT_UUID))
                .thenReturn(Optional.empty());

        // Then
        assertThrows(NoSuchElementException.class, () -> studentService.getById(StudentTestData.STUDENT_ID));
    }

    @Test
    void getAllStudentsReturnsListOfStudents() {
        // Given
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<StudentDetailsEntity> studentDetailsEntities = new PageImpl<>(Collections.singletonList(studentDetailsEntity));
        when(studentRepository.findAll(pageRequest)).thenReturn(studentDetailsEntities);

        // When
        List<Student> actualStudentDetails = studentService.getAll(0, 10);

        // Then
        assertEquals(studentDetailsEntities.getTotalElements(), actualStudentDetails.size());
        assertEquals(studentDetailsEntities.get().findFirst().get().getId(), UUID.fromString(actualStudentDetails.get(0).getId()));
    }

    @Test
    void getAllStudentsReturnsEmptyList() {
        // Given
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<StudentDetailsEntity> studentDetailsEntities = new PageImpl<>(Collections.emptyList());
        when(studentRepository.findAll(pageRequest)).thenReturn(studentDetailsEntities);

        // When
        Page<StudentDetailsEntity> actualStudentDetails = studentRepository.findAll(pageRequest);

        // Then
        assertTrue(actualStudentDetails.isEmpty());
    }

    @Test
    void createStudentThrowsException() {
        // Given
        when(studentRepository.findByCnp(StudentTestData.TEST_CNP)).thenReturn(Collections.singletonList(studentDetailsEntity));

        // Then
        assertThrows(IllegalStateException.class, () -> studentService.create(studentEntity));
    }

    @Test
    void createStudentReturnsCorrectEntity() {
        // Given
        when(studentRepository.findByCnp(StudentTestData.TEST_CNP)).thenReturn(Collections.emptyList());
        when(studentRepository.save(any(StudentDetailsEntity.class))).thenReturn(studentDetailsEntity);

        // When
        Student actualStudent = studentService.create(studentEntity);

        // Then
        assertEquals(studentFromJson.getId(), actualStudent.getId());
        assertEquals(studentFromJson.getCnp(), actualStudent.getCnp());
    }

    // Review this test
    @Test
    void updateStudentIsSuccessful() {
        // Given
        when(studentRepository.findById(StudentTestData.STUDENT_UUID))
                .thenReturn(Optional.of(studentDetailsEntity));
        when(studentRepository.save(any(StudentDetailsEntity.class)))
                .thenReturn(studentDetailsEntity);

        // When
        studentService.update(StudentTestData.STUDENT_ID, studentEntity);

        // Then
        ArgumentCaptor<StudentDetailsEntity> captor = ArgumentCaptor.forClass(StudentDetailsEntity.class);
        verify(studentRepository).save(captor.capture());
        assertEquals(StudentTestData.STUDENT_UUID, captor.getValue().getId());
    }

    @Test
    void updateStudentThrowsException() {
        // Given
        when(studentRepository.findById(StudentTestData.STUDENT_UUID)).thenReturn(Optional.empty());

        // Then
        assertThrows(NoSuchElementException.class,
                () -> studentService.update(StudentTestData.STUDENT_ID, studentEntity));
    }

    @Test
    void deleteUserIsSuccessful() {
        // Given
        when(studentRepository.findById(StudentTestData.STUDENT_UUID))
                .thenReturn(Optional.of(studentDetailsEntity));

        // When
        studentService.delete(StudentTestData.STUDENT_ID);

        // Then
        verify(studentRepository).delete(studentDetailsEntity);
    }

    @Test
    void deleteUserThrowsException() {
        // Given
        when(studentRepository.findById(StudentTestData.STUDENT_UUID)).thenReturn(Optional.empty());

        // Then
        assertThrows(NoSuchElementException.class, () -> studentService.delete(StudentTestData.STUDENT_ID));
    }

    @Test
    void updateIsValidFlagIsSuccessful() {
        // Given
        when(studentRepository.findById(StudentTestData.STUDENT_UUID))
                .thenReturn(Optional.of(studentDetailsEntity));
        studentDetailsEntity.setValid(true);
        when(studentRepository.save(any(StudentDetailsEntity.class)))
                .thenReturn(studentDetailsEntity);

        // When
        studentService.updateIsValidFlag(StudentTestData.STUDENT_ID, true);

        // Then
        ArgumentCaptor<StudentDetailsEntity> captor = ArgumentCaptor.forClass(StudentDetailsEntity.class);
        verify(studentRepository).save(captor.capture());
        assertEquals(studentDetailsEntity.getId(), captor.getValue().getId());
        assertEquals(studentDetailsEntity.isValid(), captor.getValue().isValid());
    }

    @Test
    void updateIsValidFlagIsNotPerformed() {
        // Given
        when(studentRepository.findById(StudentTestData.STUDENT_UUID)).thenReturn(Optional.empty());

        // When
        studentService.updateIsValidFlag(StudentTestData.STUDENT_ID, true);

        // Then
        ArgumentCaptor<StudentDetailsEntity> captor = ArgumentCaptor.forClass(StudentDetailsEntity.class);
        verify(studentRepository, never()).save(captor.capture());
    }
}
