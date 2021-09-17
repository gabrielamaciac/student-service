package com.learning.student.studentservice.it.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.student.studentservice.controller.api.StudentApi;
import com.learning.student.studentservice.controller.api.StudentController;
import com.learning.student.studentservice.controller.model.Student;
import com.learning.student.studentservice.exception.IllegalStateExceptionHandler;
import com.learning.student.studentservice.exception.NoSuchElementExceptionHandler;
import com.learning.student.studentservice.facade.StudentFacade;
import com.learning.student.studentservice.persistance.model.StudentEntity;
import com.learning.student.studentservice.util.StudentTestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class StudentControllerIntegrationTest {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final StudentEntity expectedStudentEntity = StudentTestData.getStudentEntity();

    @Autowired
    private StudentFacade studentFacade;

    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        StudentApi studentController = new StudentController(studentFacade);
        mvc = MockMvcBuilders.standaloneSetup(studentController)
                .setControllerAdvice(new NoSuchElementExceptionHandler(), new IllegalStateExceptionHandler())
                .build();
    }

    @Test
    void testCreateStudent() throws Exception {
        // 1. Create the student and expect status CREATED
        String randomCnp = UUID.randomUUID().toString();
        MockHttpServletResponse response = mvc.perform(post("/student")
                .contentType(MediaType.APPLICATION_JSON)
                .content(StudentTestData.getStudentJson(randomCnp)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse();

        Student actualStudent = objectMapper.readValue(response.getContentAsString(), Student.class);

        assertEquals(expectedStudentEntity.getFirstName(), actualStudent.getFirstName());
        assertEquals(randomCnp, actualStudent.getCnp());

        // 2. Try to create it again and expect status CONFLICT
        mvc.perform(post("/student")
                .contentType(MediaType.APPLICATION_JSON)
                .content(response.getContentAsString()))
                .andExpect(status().isConflict());
    }

    @Test
    void testUpdateStudentAndGetById() throws Exception {
        // 1. Create the student and expect status CREATED
        MockHttpServletResponse createdResponse = mvc.perform(post("/student")
                .contentType(MediaType.APPLICATION_JSON)
                .content(StudentTestData.STUDENT_JSON))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse();

        Student createdStudent = objectMapper.readValue(createdResponse.getContentAsString(), Student.class);

        // 2. Update the student and expect status NO_CONTENT
        mvc.perform(put("/student/" + createdStudent.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(StudentTestData.STUDENT_JSON_UPDATED))
                .andExpect(status().isNoContent());

        // 3. Retrieve by id and expect status OK, check if it was updated
        MockHttpServletResponse getByIdResponse = mvc.perform(get("/student/" + createdStudent.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        Student updatedStudent = objectMapper.readValue(getByIdResponse.getContentAsString(), Student.class);

        assertEquals("Test FirstName Updated", updatedStudent.getFirstName());
    }

    @Test
    void testGetAllStudents() throws Exception {
        // Get all students and expect status OK
        mvc.perform(get("/student?pageNo=0&pageSize=5")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteStudent() throws Exception {
        // 1. Create the student and expect status CREATED
        MockHttpServletResponse createdResponse = mvc.perform(post("/student")
                .contentType(MediaType.APPLICATION_JSON)
                .content(StudentTestData.getStudentJson(UUID.randomUUID().toString())))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse();

        Student createdStudent = objectMapper.readValue(createdResponse.getContentAsString(), Student.class);

        // 2. Delete the student and expect status NO_CONTENT
        mvc.perform(delete("/student/" + createdStudent.getId()))
                .andExpect(status().isNoContent());

        // 3. Get the student by id and expect status NOT_FOUND
        mvc.perform(get("/student/" + createdStudent.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}