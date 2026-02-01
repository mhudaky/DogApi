package org.mhudaky.dogapi.repository;

    import org.junit.jupiter.api.Test;
    import org.mhudaky.dogapi.model.Dog;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.boot.test.context.SpringBootTest;
    import org.springframework.transaction.annotation.Transactional;

    import static org.junit.jupiter.api.Assertions.assertEquals;
    import static org.junit.jupiter.api.Assertions.assertNotNull;

    @SpringBootTest
    @Transactional
    public class DogRepositoryTest {

        @Autowired
        private DogRepository dogRepository;

        @Test
        public void testSaveDog() {
            Dog dog = new Dog();
            dog.setName("Buddy");
            dog.setBreed("Golden Retriever");
            dog.setImage("http://example.com/image.jpg");

            Dog savedDog = dogRepository.save(dog);
            assertNotNull(savedDog.getId());

            Dog retrievedDog = dogRepository.findById(savedDog.getId()).orElse(null);
            assertNotNull(retrievedDog);
            assertEquals("Buddy", retrievedDog.getName());
            assertEquals("Golden Retriever", retrievedDog.getBreed());
            assertEquals("http://example.com/image.jpg", retrievedDog.getImage());
        }
    }