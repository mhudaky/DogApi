package org.mhudaky.dogapi.scheduler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mhudaky.dogapi.service.DogImageService;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DogImageUpdateSchedulerTest {

    @Mock
    private DogImageService dogImageService;

    @InjectMocks
    private DogImageUpdateScheduler scheduler;

    @Test
    void testUpdateDogImage() {
        // Act
        scheduler.updateDogImage();

        // Assert
        verify(dogImageService, times(1)).findAndUpdateDogImage();
    }
}