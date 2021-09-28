package com.learning.student.studentservice.it.api.testcontainers;

import com.learning.student.studentservice.StudentServiceApplication;
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
import org.modelmapper.ModelMapper;
import org.modelmapper.internal.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = StudentServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudentServiceApiTest extends ContainersEnvironment {

    @Autowired
    private StudentFacade studentFacade;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        StudentApi studentController = new StudentController(studentFacade, modelMapper);
        mvc = MockMvcBuilders.standaloneSetup(studentController)
                .setControllerAdvice(new NoSuchElementExceptionHandler(), new IllegalStateExceptionHandler())
                .build();
    }

    @Test
    void testCreateStudentFlow() throws Exception {
        // 1. Create the student and expect status CREATED
        int rowsBefore = JdbcTestUtils.countRowsInTable(jdbcTemplate, "students");

        String randomCnp = UUID.randomUUID().toString();
        MockHttpServletResponse result = mvc.perform(post("/student")
                .contentType(MediaType.APPLICATION_JSON)
                .content(StudentTestData.getStudentJson(randomCnp)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse();

        int rowsAfter = JdbcTestUtils.countRowsInTable(jdbcTemplate, "students");

        String createdStudentId = result.getContentAsString().substring(7, 43);
        Student createdStudent = studentFacade.getById(createdStudentId);

        Assert.notNull(result.getContentAsString());
        Assert.notNull(createdStudent);
        Assert.isTrue(rowsBefore + 1 == rowsAfter);
        assertEquals("Test FirstName", createdStudent.getFirstName());
        assertEquals(randomCnp, createdStudent.getCnp());

        // 2. Try to create it again and expect status CONFLICT
        mvc.perform(post("/student")
                .contentType(MediaType.APPLICATION_JSON)
                .content(result.getContentAsString()))
                .andExpect(status().isConflict());

        // Rows in table stayed the same
        assertEquals(rowsAfter, JdbcTestUtils.countRowsInTable(jdbcTemplate, "students"));
    }

    @Test
    void testFindByIdFlow() throws Exception {
        // 1. Retrieve by id and expect status NOT_FOUND
        mvc.perform(get("/student/" + UUID.randomUUID())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        // 2. Create the student
        StudentEntity studentEntity = StudentTestData.getStudentEntity(UUID.randomUUID().toString());
        Student createdStudent = studentFacade.create(studentEntity);

        // 3. Retrieve by id and expect status OK
        MockHttpServletResponse getByIdResponse = mvc.perform(get("/student/" + createdStudent.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        Assert.notNull(getByIdResponse.getContentAsString());
    }

    @Test
    void testUpdateStudentFlow() throws Exception {
        // 1. Create the student first
        StudentEntity studentEntity = StudentTestData.getStudentEntity(UUID.randomUUID().toString());
        Student createdStudent = studentFacade.create(studentEntity);

        // 2. Update the student and expect status NO_CONTENT
        mvc.perform(put("/student/" + createdStudent.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(StudentTestData.STUDENT_JSON_UPDATED))
                .andExpect(status().isNoContent());

        // 3. Retrieve by id and check if it was updated
        Student updatedStudent = studentFacade.getById(createdStudent.getId());
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
        // 1. Create the student first
        StudentEntity studentEntity = StudentTestData.getStudentEntity(UUID.randomUUID().toString());
        Student createdStudent = studentFacade.create(studentEntity);

        // 2. Delete the student and expect status NO_CONTENT
        mvc.perform(delete("/student/" + createdStudent.getId()))
                .andExpect(status().isNoContent());

        // 3. Get the student by id and expect null
        assertNull(studentFacade.getById(createdStudent.getId()));
    }
}
