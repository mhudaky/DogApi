package org.mhudaky.dogapi.service;

import org.mhudaky.dogapi.model.Dog;
import org.mhudaky.dogapi.repository.DogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class DogImageService {

    @Autowired
    private DogRepository dogRepository;

    @Autowired
    private RestTemplate restTemplate;

    public Optional<Dog> findAndUpdateDogImage() {
        List<Dog> dogs = dogRepository.findAll();
        for (Dog dog : dogs) {
            if (dog.getImage() == null || dog.getImage().isEmpty()) {
                String imageUrl = fetchRandomDogImage();
                if (imageUrl != null) {
                    dog.setImage(imageUrl);
                    return Optional.of(dogRepository.save(dog));
                }
            }
        }
        return Optional.empty();
    }

    private String fetchRandomDogImage() {
        String apiUrl = "https://dog.ceo/api/breeds/image/random";
        try {
            Map<String, Object> response = restTemplate.getForObject(apiUrl, Map.class);
            if ("success".equals(response.get("status"))) {
                return (String) response.get("message");
            }
        } catch (Exception e) {
            // Log error if needed
        }
        return null;
    }
}