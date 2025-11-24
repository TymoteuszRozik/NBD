package com.library.repository;

import com.library.model.Book;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends MongoRepository<Book, String> {

    Optional<Book> findByIsbn(String isbn);

    @Query("{ 'authors.name': { '$regex': ?0, '$options': 'i' } }")
    List<Book> findByAuthorName(String authorName);

    @Query("{ 'publisher': { '$regex': ?0, '$options': 'i' } }")
    List<Book> findByPublisher(String publisher);

    List<Book> findByPublicationYearBetween(Integer startYear, Integer endYear);
}