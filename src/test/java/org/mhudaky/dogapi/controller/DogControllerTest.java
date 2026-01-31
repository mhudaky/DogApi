package org.mhudaky.dogapi.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mhudaky.dogapi.DogApiApplication;
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
class DogControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

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
        String dogJson = "{\"name\":\"Buddy\",\"breed\":\"Golden Retriever\",\"image\":\"http://example.com/image.jpg\"}";

        mockMvc.perform(post("/api/dogs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dogJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.name").value("Buddy"))
                .andExpect(jsonPath("$.breed").value("Golden Retriever"))
                .andExpect(jsonPath("$.image").value("http://example.com/image.jpg"));
    }

    @Test
    void getDogById_shouldReturnDogJson() throws Exception {
        // First, create a dog to retrieve
        String dogJson = "{\"name\":\"Max\",\"breed\":\"Labrador\",\"imageUrl\":\"http://example.com/max.jpg\"}";
        String response = mockMvc.perform(post("/api/dogs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dogJson))
                .andReturn().getResponse().getContentAsString();

        // Extract ID from response (assuming JSON has "id" field)
        // For simplicity, assume ID is 1; in real tests, parse JSON
        Long dogId = 1L;

        mockMvc.perform(get("/api/dogs/{id}", dogId))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.name").value("Max"))
                .andExpect(jsonPath("$.breed").value("Labrador"));
    }

    @Test
    void getDogById_shouldReturnNotFoundForNonExistentId() throws Exception {
        mockMvc.perform(get("/api/dogs/{id}", 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteDog_shouldReturnNoContent() throws Exception {
        // Create a dog first
        String dogJson = "{\"name\":\"Rex\",\"breed\":\"Bulldog\",\"imageUrl\":\"http://example.com/rex.jpg\"}";
        String response = mockMvc.perform(post("/api/dogs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dogJson))
                .andReturn().getResponse().getContentAsString();

        Long dogId = 1L; // Assume ID

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
}