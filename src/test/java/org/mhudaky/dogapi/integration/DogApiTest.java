package org.mhudaky.dogapi.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mhudaky.dogapi.DogApiApplication;
import org.mhudaky.dogapi.model.Dog;
import org.mhudaky.dogapi.model.Gender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = DogApiApplication.class)
@AutoConfigureWebMvc
class DogApiTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void getAllDogs_shouldReturnOkAndJson() throws Exception {
        mockMvc.perform(get("/api/dogs"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void createDog_shouldReturnCreatedAndDogJson() throws Exception {
        Dog dog = sampleDog();

        mockMvc.perform(post("/api/dogs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dog)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.name").value("Buddy"))
                .andExpect(jsonPath("$.breed").value("Golden Retriever"))
                .andExpect(jsonPath("$.gender").value("MALE"))
                .andExpect(jsonPath("$.image").value("http://example.com/image.jpg"));
    }

    @Test
    void getDogById_shouldReturnDogJson() throws Exception {
        // First, create a dog to retrieve
        Dog dog = sampleDog();
        dog.setName("Max");
        dog.setBreed("Labrador");
        dog.setGender(Gender.FEMALE);
        String response = mockMvc.perform(post("/api/dogs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dog)))
                .andReturn().getResponse().getContentAsString();

        // Parse ID from response
        Long dogId = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(get("/api/dogs/{id}", dogId))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.name").value("Max"))
                .andExpect(jsonPath("$.breed").value("Labrador"))
                .andExpect(jsonPath("$.gender").value("FEMALE"));
    }

    @Test
    void getDogById_shouldReturnNotFoundForNonExistentId() throws Exception {
        mockMvc.perform(get("/api/dogs/{id}", 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteDog_shouldReturnNoContent() throws Exception {
        // Create a dog first
        Dog dog = sampleDog();
        dog.setName("Rex");
        dog.setBreed("Bulldog");
        String response = mockMvc.perform(post("/api/dogs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dog)))
                .andReturn().getResponse().getContentAsString();

        Long dogId = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(delete("/api/dogs/{id}", dogId))
                .andExpect(status().isNoContent());

        // Verify deletion by trying to retrieve
        mockMvc.perform(get("/api/dogs/{id}", dogId))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteDog_shouldReturnNotFoundForNonExistentId() throws Exception {
        mockMvc.perform(delete("/api/dogs/{id}", 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateDog_ValidInput() throws Exception {
        Dog dog = sampleDog();

        mockMvc.perform(post("/api/dogs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dog)))
                .andExpect(status().isCreated());
    }

    @Test
    void testCreateDog_InvalidName_Blank() throws Exception {
        Dog dog = sampleDog();
        dog.setName("");

        mockMvc.perform(post("/api/dogs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dog)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateDog_InvalidName_Null() throws Exception {
        Dog dog = sampleDog();
        dog.setName(null);

        mockMvc.perform(post("/api/dogs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dog)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateDog_InvalidGender_Null() throws Exception {
        Dog dog = sampleDog();
        dog.setName("Max");
        dog.setBreed("Labrador");
        dog.setGender(null);  // Invalid: null gender

        mockMvc.perform(post("/api/dogs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dog)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateDog_InvalidGender_InvalidString() throws Exception {
        String dogJson = "{\"name\":\"Max\",\"breed\":\"Labrador\",\"gender\":\"INVALID\",\"image\":\"http://example.com/max.jpg\"}";

        mockMvc.perform(post("/api/dogs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dogJson))
                .andExpect(status().isBadRequest());
    }

    private Dog sampleDog() {
        Dog dog = new Dog();
        dog.setName("Buddy");
        dog.setBreed("Golden Retriever");
        dog.setGender(Gender.MALE);
        dog.setImage("http://example.com/image.jpg");
        return dog;
    }
}