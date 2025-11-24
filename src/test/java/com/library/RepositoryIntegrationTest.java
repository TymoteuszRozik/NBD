package com.library.repository;

import com.library.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RepositoryIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PersonRepository personRepository;

    @BeforeEach
    void cleanUp() {
        personRepository.deleteAll();
    }

    @Test
    void testUserRepositoryOperations() {

        User user = User.builder()
                .firstName("Test")
                .lastName("User")
                .email("test@email.com")
                .readerCardNumber("TEST001")
                .personType("USER")
                .build();

        User saved = userRepository.save(user);

        assertNotNull(saved.getId());

        Optional<User> found = userRepository.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals("test@email.com", found.get().getEmail());
    }
}