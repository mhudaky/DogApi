package org.mhudaky.dogapi.scheduler;

import org.mhudaky.dogapi.service.DogImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DogImageUpdateScheduler {

    @Autowired
    private DogImageService dogImageService;

    @Scheduled(fixedRate = 3600000) // Every hour in milliseconds
    public void updateDogImage() {
        dogImageService.findAndUpdateDogImage();
    }
}