package com.library.repository;

import com.library.model.LibraryItem;
import com.library.model.Book;
import com.library.model.Magazine;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LibraryItemRepository extends MongoRepository<LibraryItem, String> {

    @Query("{ 'itemType': 'BOOK' }")
    List<Book> findAllBooks();

    @Query("{ 'itemType': 'MAGAZINE' }")
    List<Magazine> findAllMagazines();

    @Query("{ 'available': true, 'itemType': ?0 }")
    List<LibraryItem> findAvailableByType(String itemType);

    Optional<LibraryItem> findByIdAndAvailableTrue(String id);

    @Query("{ 'available': true }")
    List<LibraryItem> findAllAvailable();

    long countByAvailableTrue();

    @Query("{ 'title': { '$regex': ?0, '$options': 'i' } }")
    List<LibraryItem> findByTitleContaining(String title);
}