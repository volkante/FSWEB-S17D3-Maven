package com.workintech.zoo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.workintech.zoo.entity.Gender;
import com.workintech.zoo.entity.Kangaroo;
import com.workintech.zoo.entity.Koala;
import com.workintech.zoo.exceptions.ZooErrorResponse;
import com.workintech.zoo.exceptions.ZooException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(com.workintech.s17d2.ResultAnalyzer.class)
class MainTest {


    @Autowired
    private Environment env;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Kangaroo kangaroo;

    private Koala koala;

    @BeforeEach
    void setup() {

        kangaroo = new Kangaroo(1, "Kenny", 2.0, 85.0, Gender.MALE, false);
        koala = new Koala(1, "Kara", 20.0, 15.0,Gender.FEMALE);

    }

    @Test
    @DisplayName("Test Kangaroo Creation and Field Access")
     void testKangarooCreationAndFieldAccess() {

        Kangaroo kangaroo = new Kangaroo(1, "Kenny", 2.0, 85.0, "Male", false);


        assertEquals(1, kangaroo.getId());
        assertEquals("Kenny", kangaroo.getName());
        assertEquals(2.0, kangaroo.getHeight());
        assertEquals(85.0, kangaroo.getWeight());
        assertEquals("Male", kangaroo.getGender());
        assertEquals(false, kangaroo.getIsAggressive());
    }

    @Test
    @DisplayName("Test Kangaroo Setters")
    void testKangarooSetters() {

        Kangaroo kangaroo = new Kangaroo();
        kangaroo.setId(2);
        kangaroo.setName("Kanga");
        kangaroo.setHeight(1.8);
        kangaroo.setWeight(70.0);
        kangaroo.setGender("Female");
        kangaroo.setIsAggressive(true);


        assertEquals(2, kangaroo.getId());
        assertEquals("Kanga", kangaroo.getName());
        assertEquals(1.8, kangaroo.getHeight());
        assertEquals(70.0, kangaroo.getWeight());
        assertEquals("Female", kangaroo.getGender());
        assertTrue(kangaroo.getIsAggressive());
    }

    @Test
    @DisplayName("Test Koala AllArgsConstructor")
    void testKoalaAllArgsConstructor() {
        // Creating an instance using all-args constructor
        Koala koala = new Koala(1, "Kara", 20.0, 15.0, "Female");

        // Assertions to ensure fields are set correctly
        assertEquals(1, koala.getId());
        assertEquals("Kara", koala.getName());
        assertEquals(20.0, koala.getSleepHour());
        assertEquals(15.0, koala.getWeight());
        assertEquals("Female", koala.getGender());
    }

    @Test
    @DisplayName("Test Koala Setters and Getters")
    void testKoalaSettersAndGetters() {
        // Creating an instance using no-args constructor
        Koala koala = new Koala();
        koala.setId(2);
        koala.setName("Kody");
        koala.setSleepHour(22.0);
        koala.setWeight(12.5);
        koala.setGender("Male");

        // Assertions to check if setters worked through getters
        assertEquals(2, koala.getId(), "ID should match the set value.");
        assertEquals("Kody", koala.getName(), "Name should match the set value.");
        assertEquals(22.0, koala.getSleepHour(), "Sleep hour should match the set value.");
        assertEquals(12.5, koala.getWeight(), "Weight should match the set value.");
        assertEquals("Male", koala.getGender(), "Gender should match the set value.");
    }

    @Test
    @DisplayName("Test ZooErrorResponse NoArgsConstructor")
    void testNoArgsConstructor() {

        ZooErrorResponse errorResponse = new ZooErrorResponse();
        errorResponse.setStatus(400);
        errorResponse.setMessage("Bad Request");
        errorResponse.setTimestamp(System.currentTimeMillis());


        assertEquals(400, errorResponse.getStatus());
        assertEquals("Bad Request", errorResponse.getMessage());

    }

    @Test
    @DisplayName("Test ZooErrorResponse AllArgsConstructor")
     void testAllArgsConstructor() {

        long now = System.currentTimeMillis();


        ZooErrorResponse errorResponse = new ZooErrorResponse(404, "Not Found", now);


        assertEquals(404, errorResponse.getStatus());
        assertEquals("Not Found", errorResponse.getMessage());
        assertEquals(now, errorResponse.getTimestamp());
    }

    @Test
    @DisplayName("Test ZooException Creation")
    void testZooExceptionCreation() {
        String expectedMessage = "Test exception message";
        HttpStatus expectedStatus = HttpStatus.NOT_FOUND;

        ZooException exception = new ZooException(expectedMessage, expectedStatus);


        assertEquals(expectedMessage, exception.getMessage(), "The exception message should match the expected value.");
        assertEquals(expectedStatus, exception.getHttpStatus(), "The HttpStatus should match the expected value.");


        assertTrue(exception instanceof RuntimeException, "ZooException should be an instance of RuntimeException.");
    }

    @Test
    @DisplayName("Test ZooException HttpStatus Setter")
    void testHttpStatusSetter() {
        ZooException exception = new ZooException("Initial message", HttpStatus.OK);
        exception.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);


        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getHttpStatus(), "The HttpStatus should be updatable and match the new value.");
    }

    @Test
    @DisplayName("application properties istenilenler eklendi mi?")
    void serverPortIsSetTo8585() {

        String serverPort = env.getProperty("server.port");
        assertThat(serverPort).isEqualTo("9000");

        String contextPath = env.getProperty("server.servlet.context-path");
        assertNotNull(contextPath);
        assertThat(contextPath).isEqualTo("/workintech");

    }


    @Test
    @DisplayName("KangarooController:SaveKangaroo")
    @Order(1)
    void testSaveKangaroo() throws Exception {
        mockMvc.perform(post("/kangaroos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(kangaroo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(kangaroo.getId()))
                .andExpect(jsonPath("$.name").value(kangaroo.getName()));
    }

    @Test
    @DisplayName("KangarooController:FindAllKangaroos")
    @Order(2)
    void testFindAllKangaroos() throws Exception {

        mockMvc.perform(post("/kangaroos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(kangaroo)));

        mockMvc.perform(get("/kangaroos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(kangaroo.getId()));
    }

    @Test
    @DisplayName("KangarooController:FindKangarooById")
    @Order(3)
    void testFindKangarooById() throws Exception {

        mockMvc.perform(post("/kangaroos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(kangaroo)));

        mockMvc.perform(get("/kangaroos/{id}", kangaroo.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(kangaroo.getId()));
    }

    @Test
    @DisplayName("KangarooController:UpdateKangaroo")
    @Order(4)
    void testUpdateKangaroo() throws Exception {

        mockMvc.perform(post("/kangaroos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(kangaroo)));


        kangaroo.setName("Updated Kenny");
        mockMvc.perform(put("/kangaroos/{id}", kangaroo.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(kangaroo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Kenny"));
    }

    @Test
    @DisplayName("KangarooController:DeleteKangaroo")
    @Order(5)
    void testDeleteKangaroo() throws Exception {

        mockMvc.perform(post("/kangaroos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(kangaroo)));


        mockMvc.perform(delete("/kangaroos/{id}", kangaroo.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(kangaroo.getId()));
    }

    @Test
    @DisplayName("KoalaController:SaveKoala")
    @Order(6)
    void testSaveKoala() throws Exception {
        mockMvc.perform(post("/koalas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(koala)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(koala.getId()))
                .andExpect(jsonPath("$.name").value(koala.getName()));
    }

    @Test
    @DisplayName("KoalaController:FindAllKoalas")
    @Order(7)
    void testFindAllKoalas() throws Exception {

        mockMvc.perform(post("/koalas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(koala)));

        mockMvc.perform(get("/koalas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(koala.getId()));
    }

    @Test
    @DisplayName("KoalaController:FindKoalaById")
    @Order(8)
    void testFindKoalaById() throws Exception {

        mockMvc.perform(post("/koalas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(koala)));

        mockMvc.perform(get("/koalas/{id}", koala.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(koala.getId()));
    }

    @Test
    @DisplayName("KoalaController:UpdateKoala")
    @Order(9)
    void testUpdateKoala() throws Exception {

        mockMvc.perform(post("/koalas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(koala)));


        koala.setName("Updated Kara");
        mockMvc.perform(put("/koalas/{id}", koala.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(koala)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Kara"));
    }

    @Test
    @DisplayName("KoalaController:DeleteKoala")
    @Order(10)
    void testDeleteKoala() throws Exception {

        mockMvc.perform(post("/koalas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(koala)));

        mockMvc.perform(delete("/koalas/{id}", koala.getId()))
                .andExpect(status().isOk());
    }


    @Test
    @DisplayName("ZooGlobalExceptionHandler:HandleZooException")
    void testHandleZooException() throws Exception {

        int nonExistingId = 999;

        mockMvc.perform(get("/kangaroos/{id}", nonExistingId))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").isNotEmpty())
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    @DisplayName("ZooGlobalExceptionHandler:HandleGenericException")
    void testHandleGenericException() throws Exception {
    Kangaroo invalidKangaroo = new Kangaroo();
    mockMvc.perform(post("/kangaroos")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidKangaroo)))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message").isNotEmpty())
            .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()));
    }
}
