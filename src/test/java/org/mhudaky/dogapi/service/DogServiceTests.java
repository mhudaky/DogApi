package org.mhudaky.dogapi.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mhudaky.dogapi.model.Dog;
import org.mhudaky.dogapi.repository.DogRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DogServiceTest {

    @Mock
    private DogRepository dogRepository;

    @InjectMocks
    private DogService dogService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllDogs() {
        // Arrange
        Dog dog1 = new Dog();
        Dog dog2 = new Dog();
        List<Dog> dogs = Arrays.asList(dog1, dog2);
        when(dogRepository.findAll()).thenReturn(dogs);

        // Act
        List<Dog> result = dogService.getAllDogs();

        // Assert
        assertEquals(2, result.size());
        verify(dogRepository, times(1)).findAll();
    }

    @Test
    void testGetDogById_Found() {
        // Arrange
        Dog dog = new Dog();
        when(dogRepository.findById(1L)).thenReturn(Optional.of(dog));

        // Act
        Optional<Dog> result = dogService.getDogById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(dog, result.get());
        verify(dogRepository, times(1)).findById(1L);
    }

    @Test
    void testGetDogById_NotFound() {
        // Arrange
        when(dogRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Optional<Dog> result = dogService.getDogById(1L);

        // Assert
        assertFalse(result.isPresent());
        verify(dogRepository, times(1)).findById(1L);
    }

    @Test
    void testCreateDog() {
        // Arrange
        Dog dog = new Dog();
        Dog savedDog = new Dog();
        when(dogRepository.save(dog)).thenReturn(savedDog);

        // Act
        Dog result = dogService.createDog(dog);

        // Assert
        assertEquals(savedDog, result);
        verify(dogRepository, times(1)).save(dog);
    }

    @Test
    void testDeleteDog() {
        // Act
        dogService.deleteDog(1L);

        // Assert
        verify(dogRepository, times(1)).deleteById(1L);
    }
}