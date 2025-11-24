package com.library.repository;

import com.library.model.Person;
import com.library.model.User;
import com.library.model.Librarian;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository extends MongoRepository<Person, String> {

    Optional<Person> findByEmail(String email);

    @Query("{ 'person_type': 'USER' }")
    List<User> findAllUsers();

    @Query("{ 'person_type': 'LIBRARIAN' }")
    List<Librarian> findAllLibrarians();

    @Query("{ 'email': ?0, 'person_type': 'USER' }")
    Optional<User> findUserByEmail(String email);

    @Query("{ 'reader_card_number': ?0 }")
    Optional<User> findByReaderCardNumber(String readerCardNumber);

    boolean existsByEmail(String email);
}