package com.library.service;

import com.library.model.*;
import com.library.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoanService {

    private final LoanRepository loanRepository;
    private final UserRepository userRepository;
    private final LibraryItemRepository itemRepository;
    private final MongoTemplate mongoTemplate;

    @Transactional
    public Loan borrowItem(String userId, String itemId, LocalDate dueDate) {
        log.info("Attempting to borrow item {} for user {}", itemId, userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getActiveLoansCount() >= user.getMaxAllowedLoans()) {
            throw new RuntimeException("User has reached maximum loan limit");
        }

        LibraryItem item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        if (!item.getAvailable()) {
            throw new RuntimeException("Item is not available for loan");
        }

        Loan loan = Loan.builder()
                .userId(userId)
                .itemId(itemId)
                .itemTitle(item.getTitle())
                .itemType(item.getItemType())
                .loanDate(LocalDate.now())
                .dueDate(dueDate)
                .status(Loan.LoanStatus.ACTIVE)
                .build();

        Loan savedLoan = loanRepository.save(loan);

        item.setAvailable(false);
        item.setCurrentLoanId(savedLoan.getId());
        itemRepository.save(item);

        userRepository.incrementActiveLoansCount(userId);

        log.info("Successfully created loan {} for user {}", savedLoan.getId(), userId);
        return savedLoan;
    }

    @Transactional
    public void returnItem(String loanId) {
        log.info("Attempting to return loan {}", loanId);

        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        if (loan.getStatus() == Loan.LoanStatus.RETURNED) {
            throw new RuntimeException("Item is already returned");
        }

        loan.setStatus(Loan.LoanStatus.RETURNED);
        loan.setReturnDate(LocalDate.now());
        loanRepository.save(loan);

        LibraryItem item = itemRepository.findById(loan.getItemId())
                .orElseThrow(() -> new RuntimeException("Item not found"));
        item.setAvailable(true);
        item.setCurrentLoanId(null);
        itemRepository.save(item);

        userRepository.decrementActiveLoansCount(loan.getUserId());

        log.info("Successfully returned loan {}", loanId);
    }

    public boolean canUserBorrowItem(String userId, String itemId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<LibraryItem> item = itemRepository.findById(itemId);

        return user.isPresent() &&
                item.isPresent() &&
                user.get().getActiveLoansCount() < user.get().getMaxAllowedLoans() &&
                item.get().getAvailable();
    }

    public List<Loan> getActiveLoansByUser(String userId) {
        return loanRepository.findByUserIdAndStatus(userId, Loan.LoanStatus.ACTIVE);
    }

    public List<Loan> getOverdueLoans() {
        return loanRepository.findByDueDateBeforeAndStatus(LocalDate.now(), Loan.LoanStatus.ACTIVE);
    }
}