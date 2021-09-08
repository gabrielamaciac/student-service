package com.learning.student.studentservice.controller.api;

import com.learning.student.studentservice.controller.model.Student;
import com.learning.student.studentservice.exception.IllegalStateExceptionHandler;
import com.learning.student.studentservice.exception.NoSuchElementExceptionHandler;
import com.learning.student.studentservice.facade.StudentFacade;
import com.learning.student.studentservice.persistance.model.StudentEntity;
import com.learning.student.studentservice.util.StudentTestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

public class StudentControllerTest {
    private MockMvc mvc;
    private StudentFacade studentFacade;
    private StudentController studentController;

    private StudentEntity expectedStudentEntity = StudentTestData.getStudentEntity();
    private Student expectedStudent = StudentTestData.getStudent();

    @BeforeEach
    void setUp() {
        studentFacade = mock(StudentFacade.class);
        studentController = new StudentController(studentFacade);
        mvc = MockMvcBuilders.standaloneSetup(studentController)
                .setControllerAdvice(new NoSuchElementExceptionHandler(), new IllegalStateExceptionHandler())
                .build();
    }

    @Test
    public void createStudentReturns201StatusCode() throws Exception {
        // given
        given(studentFacade.create(any(StudentEntity.class))).willReturn(expectedStudent);

        // when
        MockHttpServletResponse response = mvc.perform(post("/student")
                .contentType(MediaType.APPLICATION_JSON).content(StudentTestData.STUDENT_JSON))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    public void createStudentReturns409StatusCode() throws Exception {
        // given
        doThrow(new IllegalStateException()).when(studentFacade).create(any(StudentEntity.class));

        // when
        MockHttpServletResponse response = mvc.perform(post("/student")
                .contentType(MediaType.APPLICATION_JSON).content(StudentTestData.STUDENT_JSON))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CONFLICT.value());
    }

    @Test
    void getStudentByIdReturns200StatusCode() throws Exception {
        // given
        given(studentFacade.getById(StudentTestData.STUDENT_ID)).willReturn(expectedStudent);

        // when
        MockHttpServletResponse response = mvc.perform(get("/student/" + StudentTestData.STUDENT_ID)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void getStudentByIdReturns404StatusCode() throws Exception {
        // given
        given(studentFacade.getById(StudentTestData.STUDENT_ID)).willThrow(new NoSuchElementException());

        // when
        MockHttpServletResponse response = mvc.perform(get("/student/" + StudentTestData.STUDENT_ID)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(response.getContentAsString()).isEmpty();
    }

    @Test
    void getAllStudentsReturns200StatusCode() throws Exception {
        // given
        List<Student> expectedStudentList = Arrays.asList(expectedStudent);
        given(studentFacade.getAll(0, 10)).willReturn(expectedStudentList);

        // when
        MockHttpServletResponse response = mvc.perform(get("/student?pageNo=0&pageSize=10")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void updateStudentReturns204StatusCode() throws Exception {
        // when
        MockHttpServletResponse response = mvc.perform(put("/student/" + StudentTestData.STUDENT_ID)
                .contentType(MediaType.APPLICATION_JSON).content(StudentTestData.STUDENT_JSON))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @Test
    public void updateStudentReturns404StatusCode() throws Exception {
        // given
        doThrow(new NoSuchElementException()).when(studentFacade).update(any(String.class), any(StudentEntity.class));

        // when
        MockHttpServletResponse response = mvc.perform(put("/student/" + StudentTestData.STUDENT_ID)
                .contentType(MediaType.APPLICATION_JSON).content(StudentTestData.STUDENT_JSON))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void deleteStudentReturns204StatusCode() throws Exception {
        // when
        MockHttpServletResponse response = mvc.perform(delete("/student/" + StudentTestData.STUDENT_ID)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @Test
    public void deleteStudentReturns404StatusCode() throws Exception {
        // given
        doThrow(new NoSuchElementException()).when(studentFacade).delete(any(String.class));

        // when
        MockHttpServletResponse response = mvc.perform(delete("/student/" + StudentTestData.STUDENT_ID)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

}
