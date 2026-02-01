package org.mhudaky.dogapi.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mhudaky.dogapi.model.Dog;
import org.mhudaky.dogapi.repository.DogRepository;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DogImageServiceTest {

    @Mock
    private DogRepository dogRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private DogImageService dogImageService;

    @Test
    void testFindAndUpdateDogImage_Success() {
        // Arrange
        Dog dog1 = new Dog(); // Assume Dog has a constructor and setters
        dog1.setImage("existing.jpg");
        Dog dog2 = new Dog();
        dog2.setImage(null);
        List<Dog> dogs = Arrays.asList(dog1, dog2);
        when(dogRepository.findAll()).thenReturn(dogs);

        Map<String, Object> mockResponse = Map.of("status", "success", "message", "https://example.com/dog.jpg");
        when(restTemplate.getForObject(anyString(), eq(Map.class))).thenReturn(mockResponse);

        Dog savedDog = new Dog();
        savedDog.setImage("https://example.com/dog.jpg");
        when(dogRepository.save(any(Dog.class))).thenReturn(savedDog);

        // Act
        Optional<Dog> result = dogImageService.findAndUpdateDogImage();

        // Assert
        assertTrue(result.isPresent());
        assertEquals("https://example.com/dog.jpg", result.get().getImage());
        verify(dogRepository, times(1)).findAll();
        verify(restTemplate, times(1)).getForObject("https://dog.ceo/api/breeds/image/random", Map.class);
        verify(dogRepository, times(1)).save(dog2);
    }

    @Test
    void testFindAndUpdateDogImage_NoDogWithoutImage() {
        // Arrange
        Dog dog1 = new Dog();
        dog1.setImage("existing.jpg");
        List<Dog> dogs = Arrays.asList(dog1);
        when(dogRepository.findAll()).thenReturn(dogs);

        // Act
        Optional<Dog> result = dogImageService.findAndUpdateDogImage();

        // Assert
        assertFalse(result.isPresent());
        verify(dogRepository, times(1)).findAll();
        verify(restTemplate, never()).getForObject(anyString(), any());
        verify(dogRepository, never()).save(any(Dog.class));
    }

    @Test
    void testFindAndUpdateDogImage_ApiFailure() {
        // Arrange
        Dog dog1 = new Dog();
        dog1.setImage(null);
        List<Dog> dogs = Arrays.asList(dog1);
        when(dogRepository.findAll()).thenReturn(dogs);
        when(restTemplate.getForObject(anyString(), eq(Map.class))).thenThrow(new RuntimeException("API error"));

        // Act
        Optional<Dog> result = dogImageService.findAndUpdateDogImage();

        // Assert
        assertFalse(result.isPresent());
        verify(dogRepository, times(1)).findAll();
        verify(restTemplate, times(1)).getForObject("https://dog.ceo/api/breeds/image/random", Map.class);
        verify(dogRepository, never()).save(any(Dog.class));
    }
}