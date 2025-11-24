package com.library.repository;

import com.library.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DataMappingTest {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Test
    void testInheritanceMapping() {
        User user = User.builder()
                .firstName("User")
                .lastName("Test")
                .email("user@test.com")
                .personType("USER")
                .readerCardNumber("USER001")
                .build();

        Librarian librarian = Librarian.builder()
                .firstName("Admin")
                .lastName("System")
                .email("admin@test.com")
                .personType("LIBRARIAN")
                .employeeId("EMP001")
                .department("IT")
                .build();

        User savedUser = (User) personRepository.save(user);
        Librarian savedLibrarian = (Librarian) personRepository.save(librarian);

        List<User> allUsers = personRepository.findAllUsers();
        List<Librarian> allLibrarians = personRepository.findAllLibrarians();

        assertFalse(allUsers.isEmpty());
        assertFalse(allLibrarians.isEmpty());
        assertEquals("USER", allUsers.get(0).getPersonType());
        assertEquals("LIBRARIAN", allLibrarians.get(0).getPersonType());
    }

    @Test
    void testEmbeddedDocumentsMapping() {
        Book book = Book.builder()
                .title("Test Embedded Mapping")
                .isbn("978-embedded-123")
                .publicationYear(2023)
                .itemType("BOOK")
                .available(true)
                .authors(List.of(
                        new Book.EmbeddedAuthor("auth1", "Author One", "Polish"),
                        new Book.EmbeddedAuthor("auth2", "Author Two", "American")
                ))
                .build();

        Book savedBook = bookRepository.save(book);

        Book foundBook = bookRepository.findById(savedBook.getId()).orElseThrow();

        assertEquals(2, foundBook.getAuthors().size());
        assertEquals("Author One", foundBook.getAuthors().get(0).getName());
        assertEquals("Polish", foundBook.getAuthors().get(0).getNationality());
        assertEquals("Author Two", foundBook.getAuthors().get(1).getName());
    }

    @Test
    void testReferenceMapping() {
        Author author = Author.builder()
                .name("Reference Author")
                .nationality("French")
                .build();

        Author savedAuthor = authorRepository.save(author);

        Book book1 = Book.builder()
                .title("Book One")
                .isbn("978-ref-001")
                .publicationYear(2020)
                .itemType("BOOK")
                .authors(List.of(new Book.EmbeddedAuthor(savedAuthor.getId(), "Reference Author", "French")))
                .build();

        Book book2 = Book.builder()
                .title("Book Two")
                .isbn("978-ref-002")
                .publicationYear(2021)
                .itemType("BOOK")
                .authors(List.of(new Book.EmbeddedAuthor(savedAuthor.getId(), "Reference Author", "French")))
                .build();

        Book savedBook1 = bookRepository.save(book1);
        Book savedBook2 = bookRepository.save(book2);

        savedAuthor.getBookIds().add(savedBook1.getId());
        savedAuthor.getBookIds().add(savedBook2.getId());
        authorRepository.save(savedAuthor);

        List<Author> authorsWithTwoBooks = authorRepository.findByBookCount(2);
        assertFalse(authorsWithTwoBooks.isEmpty());
        assertEquals(2, authorsWithTwoBooks.get(0).getBookIds().size());
    }
}