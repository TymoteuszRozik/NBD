package com.library.repository;

import com.library.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByReaderCardNumber(String readerCardNumber);

    Optional<User> findByEmail(String email);

    default void incrementActiveLoansCount(String userId) {
        findById(userId).ifPresent(user -> {
            user.setActiveLoansCount(user.getActiveLoansCount() + 1);
            save(user);
        });
    }

    default void decrementActiveLoansCount(String userId) {
        findById(userId).ifPresent(user -> {
            user.setActiveLoansCount(Math.max(0, user.getActiveLoansCount() - 1));
            save(user);
        });
    }

    default Optional<User> findByIdWithLoanCapacity(String userId) {
        return findById(userId)
                .filter(user -> user.getActiveLoansCount() < user.getMaxAllowedLoans());
    }
}