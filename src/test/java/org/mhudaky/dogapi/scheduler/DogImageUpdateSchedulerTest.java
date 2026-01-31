package org.mhudaky.dogapi.scheduler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mhudaky.dogapi.service.DogImageService;

import static org.mockito.Mockito.*;

class DogImageUpdateSchedulerTest {

    @Mock
    private DogImageService dogImageService;

    @InjectMocks
    private DogImageUpdateScheduler scheduler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUpdateDogImage() {
        // Act
        scheduler.updateDogImage();

        // Assert
        verify(dogImageService, times(1)).findAndUpdateDogImage();
    }
}