package com.learning.student.studentservice.integration.queue;

import com.learning.student.studentservice.integration.model.StudentMessage;
import com.learning.student.studentservice.persistance.model.StudentEntity;
import com.learning.student.studentservice.service.StudentService;
import com.learning.student.studentservice.util.StudentTestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class StudentQueueConsumerTest {
    private StudentService studentService;
    private StudentQueueConsumer studentQueueConsumer;
    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        studentService = mock(StudentService.class);
        modelMapper = mock(ModelMapper.class);
        studentQueueConsumer = new StudentQueueConsumer(studentService, modelMapper);
    }

    @Test
    void processMessageIsSuccessful() {
        // given
        StudentEntity studentEntity = StudentTestData.getStudentEntity();
        when(modelMapper.map(any(StudentMessage.class), eq(StudentEntity.class))).thenReturn(studentEntity);

        // when
        studentQueueConsumer.receiveMessage(StudentTestData.STUDENT_JSON);

        // then
        verify(studentService).create(studentEntity);
    }

    @Test
    void processMessageIsSkipped() {
        // given
        when(modelMapper.map(any(StudentMessage.class), eq(StudentEntity.class))).thenReturn(null);

        // when
        studentQueueConsumer.receiveMessage(StudentTestData.STUDENT_JSON);

        // then
        verify(studentService, never()).create(any(StudentEntity.class));
    }
}
