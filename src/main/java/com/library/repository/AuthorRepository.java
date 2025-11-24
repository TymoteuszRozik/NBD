package com.library.repository;

import com.library.model.Author;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends MongoRepository<Author, String> {

    List<Author> findByNameContainingIgnoreCase(String name);

    List<Author> findByNationality(String nationality);

    @Query("{ 'book_ids': { '$size': ?0 } }")
    List<Author> findByBookCount(int bookCount);

    @Query("{ 'book_ids': { '$exists': true, '$ne': [] } }")
    List<Author> findAuthorsWithBooks();
}