package com.library.service;

import com.library.model.*;
import com.library.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LoanServiceTest {

    @Autowired
    private LoanService loanService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LibraryItemRepository itemRepository;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private PersonRepository personRepository;

    private User testUser;
    private Book testBook;

    @BeforeEach
    void setUp() {
        personRepository.deleteAll();
        itemRepository.deleteAll();
        loanRepository.deleteAll();

        testUser = User.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@email.com")
                .readerCardNumber("RC001")
                .maxAllowedLoans(5)
                .activeLoansCount(0)
                .personType("USER")
                .build();

        testBook = Book.builder()
                .title("Test Book")
                .publicationYear(2023)
                .isbn("978-0123456789")
                .publisher("Test Publisher")
                .available(true)
                .itemType("BOOK")
                .build();

        testUser = userRepository.save(testUser);
        testBook = itemRepository.save(testBook);
    }

    @Test
    void testSuccessfulBookLoan() {
        Loan loan = loanService.borrowItem(
                testUser.getId(),
                testBook.getId(),
                LocalDate.now().plusWeeks(2)
        );
        assertNotNull(loan);
        assertEquals(testUser.getId(), loan.getUserId());
        assertEquals(testBook.getId(), loan.getItemId());
        assertEquals(Loan.LoanStatus.ACTIVE, loan.getStatus());
    }

    @Test
    void testCanUserBorrowItem() {
        boolean canBorrow = loanService.canUserBorrowItem(testUser.getId(), testBook.getId());
        assertTrue(canBorrow, "User should be able to borrow item");
    }
}