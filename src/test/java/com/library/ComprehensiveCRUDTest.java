package com.library.repository;

import com.library.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ComprehensiveCRUDTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private LibraryItemRepository itemRepository;

    @BeforeEach
    void cleanUp() {
        personRepository.deleteAll();
        itemRepository.deleteAll();
        loanRepository.deleteAll();
        authorRepository.deleteAll();
    }

    @Test
    void testCompleteUserCRUDOperations() {
        // CREATE
        User user = User.builder()
                .firstName("Jan")
                .lastName("Kowalski")
                .email("jan.kowalski@email.com")
                .readerCardNumber("RC12345")
                .personType("USER")
                .activeLoansCount(0)
                .maxAllowedLoans(5)
                .build();

        User savedUser = userRepository.save(user);
        assertNotNull(savedUser.getId());
        assertEquals("Jan", savedUser.getFirstName());

        // READ
        Optional<User> foundUser = userRepository.findById(savedUser.getId());
        assertTrue(foundUser.isPresent());
        assertEquals("jan.kowalski@email.com", foundUser.get().getEmail());

        // READ by custom field
        Optional<User> foundByCard = userRepository.findByReaderCardNumber("RC12345");
        assertTrue(foundByCard.isPresent());

        // UPDATE
        savedUser.setFirstName("Janusz");
        savedUser.setLastName("Nowak");
        User updatedUser = userRepository.save(savedUser);
        assertEquals("Janusz", updatedUser.getFirstName());
        assertEquals("Nowak", updatedUser.getLastName());

        // UPDATE with business logic
        userRepository.incrementActiveLoansCount(updatedUser.getId());
        User afterIncrement = userRepository.findById(updatedUser.getId()).orElseThrow();
        assertEquals(1, afterIncrement.getActiveLoansCount());

        // ✅ DELETE
        userRepository.deleteById(updatedUser.getId());
        Optional<User> deletedUser = userRepository.findById(updatedUser.getId());
        assertFalse(deletedUser.isPresent());
    }

    @Test
    void testCompleteBookCRUDOperations() {
        // CREATE Book with embedded authors
        Book book = Book.builder()
                .title("Wiedźmin")
                .publicationYear(1990)
                .isbn("978-837-578-024-5")
                .publisher("SuperNowa")
                .available(true)
                .itemType("BOOK")
                .authors(List.of(
                        new Book.EmbeddedAuthor("auth1", "Andrzej Sapkowski", "Polish")
                ))
                .build();

        Book savedBook = bookRepository.save(book);
        assertNotNull(savedBook.getId());

        // READ with embedded data
        Optional<Book> foundBook = bookRepository.findById(savedBook.getId());
        assertTrue(foundBook.isPresent());
        assertEquals("Wiedźmin", foundBook.get().getTitle());
        assertEquals(1, foundBook.get().getAuthors().size());
        assertEquals("Andrzej Sapkowski", foundBook.get().getAuthors().get(0).getName());

        // READ by custom queries
        List<Book> booksByAuthor = bookRepository.findByAuthorName("Sapkowski");
        assertFalse(booksByAuthor.isEmpty());

        Optional<Book> bookByIsbn = bookRepository.findByIsbn("978-837-578-024-5");
        assertTrue(bookByIsbn.isPresent());

        // UPDATE availability
        savedBook.setAvailable(false);
        Book updatedBook = bookRepository.save(savedBook);
        assertFalse(updatedBook.getAvailable());

        // DELETE
        bookRepository.deleteById(updatedBook.getId());
        assertFalse(bookRepository.findById(updatedBook.getId()).isPresent());
    }

    @Test
    void testLoanCRUDWithBusinessRules() {
        // Setup
        User user = userRepository.save(User.builder()
                .firstName("Loan")
                .lastName("User")
                .email("loan.user@email.com")
                .personType("USER")
                .build());

        Book book = bookRepository.save(Book.builder()
                .title("Loan Test Book")
                .publicationYear(2020)
                .available(true)
                .itemType("BOOK")
                .build());

        // CREATE Loan
        Loan loan = Loan.builder()
                .userId(user.getId())
                .itemId(book.getId())
                .itemTitle(book.getTitle())
                .itemType("BOOK")
                .loanDate(LocalDate.now())
                .dueDate(LocalDate.now().plusWeeks(2))
                .status(Loan.LoanStatus.ACTIVE)
                .build();

        Loan savedLoan = loanRepository.save(loan);
        assertNotNull(savedLoan.getId());

        // READ with status filtering
        List<Loan> activeLoans = loanRepository.findByUserIdAndStatus(user.getId(), Loan.LoanStatus.ACTIVE);
        assertFalse(activeLoans.isEmpty());
        assertEquals(savedLoan.getId(), activeLoans.get(0).getId());

        // UPDATE loan status
        savedLoan.setStatus(Loan.LoanStatus.RETURNED);
        savedLoan.setReturnDate(LocalDate.now());
        Loan returnedLoan = loanRepository.save(savedLoan);
        assertEquals(Loan.LoanStatus.RETURNED, returnedLoan.getStatus());

        // READ overdue loans
        List<Loan> overdueLoans = loanRepository.findByDueDateBeforeAndStatus(LocalDate.now().minusDays(1), Loan.LoanStatus.ACTIVE);
        assertTrue(overdueLoans.isEmpty());
    }
}