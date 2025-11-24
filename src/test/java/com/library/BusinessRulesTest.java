package com.library.service;

import com.library.model.*;
import com.library.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BusinessRulesTest {

    @Autowired
    private LoanService loanService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private PersonRepository personRepository;

    private User testUser;
    private Book testBook;

    @BeforeEach
    void setUp() {
        personRepository.deleteAll();
        bookRepository.deleteAll();
        loanRepository.deleteAll();

        testUser = userRepository.save(User.builder()
                .firstName("Business")
                .lastName("Rules")
                .email("business.rules@test.com")
                .personType("USER")
                .activeLoansCount(0)
                .maxAllowedLoans(3)
                .build());

        testBook = bookRepository.save(Book.builder()
                .title("Business Rules Book")
                .isbn("978-business-001") // DODAJ ISBN
                .publicationYear(2023)
                .available(true)
                .itemType("BOOK")
                .build());
    }

    @Test
    void testConcurrentLoanPrevention() {
        Loan firstLoan = loanService.borrowItem(
                testUser.getId(),
                testBook.getId(),
                LocalDate.now().plusWeeks(2)
        );

        Book updatedBook = bookRepository.findById(testBook.getId()).orElseThrow();
        assertFalse(updatedBook.getAvailable());

        assertThrows(RuntimeException.class, () ->
                loanService.borrowItem(testUser.getId(), testBook.getId(), LocalDate.now().plusWeeks(1))
        );
    }

    @Test
    void testUserLoanLimitEnforcement() {
        Book book1 = bookRepository.save(Book.builder().title("Book 1").isbn("978-limit-001").available(true).itemType("BOOK").build());
        Book book2 = bookRepository.save(Book.builder().title("Book 2").isbn("978-limit-002").available(true).itemType("BOOK").build());
        Book book3 = bookRepository.save(Book.builder().title("Book 3").isbn("978-limit-003").available(true).itemType("BOOK").build());
        Book book4 = bookRepository.save(Book.builder().title("Book 4").isbn("978-limit-004").available(true).itemType("BOOK").build());

        loanService.borrowItem(testUser.getId(), book1.getId(), LocalDate.now().plusWeeks(1));
        loanService.borrowItem(testUser.getId(), book2.getId(), LocalDate.now().plusWeeks(1));
        loanService.borrowItem(testUser.getId(), book3.getId(), LocalDate.now().plusWeeks(1));

        User userAtLimit = userRepository.findById(testUser.getId()).orElseThrow();
        assertEquals(3, userAtLimit.getActiveLoansCount());

        assertThrows(RuntimeException.class, () ->
                loanService.borrowItem(testUser.getId(), book4.getId(), LocalDate.now().plusWeeks(1))
        );
    }

    @Test
    void testReturnItemBusinessRules() {
        Loan loan = loanService.borrowItem(testUser.getId(), testBook.getId(), LocalDate.now().plusWeeks(2));

        User userAfterBorrow = userRepository.findById(testUser.getId()).orElseThrow();
        Book bookAfterBorrow = bookRepository.findById(testBook.getId()).orElseThrow();
        assertEquals(1, userAfterBorrow.getActiveLoansCount());
        assertFalse(bookAfterBorrow.getAvailable());

        loanService.returnItem(loan.getId());

        User userAfterReturn = userRepository.findById(testUser.getId()).orElseThrow();
        Book bookAfterReturn = bookRepository.findById(testBook.getId()).orElseThrow();
        Loan returnedLoan = loanRepository.findById(loan.getId()).orElseThrow();

        assertEquals(0, userAfterReturn.getActiveLoansCount());
        assertTrue(bookAfterReturn.getAvailable());
        assertEquals(Loan.LoanStatus.RETURNED, returnedLoan.getStatus());
        assertNotNull(returnedLoan.getReturnDate());
    }

    @Test
    void testOverdueLoansDetection() {
        Loan overdueLoan = Loan.builder()
                .userId(testUser.getId())
                .itemId(testBook.getId())
                .itemTitle(testBook.getTitle())
                .itemType("BOOK")
                .loanDate(LocalDate.now().minusWeeks(3))
                .dueDate(LocalDate.now().minusWeeks(1)) // Overdue by 1 week
                .status(Loan.LoanStatus.ACTIVE)
                .build();

        loanRepository.save(overdueLoan);

        List<Loan> overdueLoans = loanService.getOverdueLoans();
        assertFalse(overdueLoans.isEmpty());
        assertEquals(overdueLoan.getId(), overdueLoans.get(0).getId());
    }
}