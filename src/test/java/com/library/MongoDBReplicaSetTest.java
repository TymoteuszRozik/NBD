package com.library.service;

import com.library.model.User;
import com.library.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MongoDBReplicaSetTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testEventualConsistency() {
        User user = User.builder()
                .firstName("Consistency")
                .lastName("Test")
                .email("consistency@test.com")
                .personType("USER")
                .build();

        User savedUser = userRepository.save(user);

        Optional<User> immediateRead = userRepository.findById(savedUser.getId());
        assertTrue(immediateRead.isPresent());

        Optional<User> readByEmail = userRepository.findByEmail("consistency@test.com");
        assertTrue(readByEmail.isPresent());
    }

    @Test
    void testDataIntegrityAfterMultipleOperations() {
        User user = userRepository.save(User.builder()
                .firstName("Integrity")
                .lastName("Test")
                .email("integrity@test.com")
                .personType("USER")
                .activeLoansCount(0)
                .maxAllowedLoans(5)
                .build());

        userRepository.incrementActiveLoansCount(user.getId());
        userRepository.incrementActiveLoansCount(user.getId());
        userRepository.decrementActiveLoansCount(user.getId());

        User finalUser = userRepository.findById(user.getId()).orElseThrow();
        assertEquals(1, finalUser.getActiveLoansCount());
    }
}