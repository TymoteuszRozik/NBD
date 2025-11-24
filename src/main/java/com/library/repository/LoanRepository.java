package com.library.repository;

import com.library.model.Loan;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface LoanRepository extends MongoRepository<Loan, String> {

    List<Loan> findByUserIdAndStatus(String userId, Loan.LoanStatus status);

    Optional<Loan> findByItemIdAndStatus(String itemId, Loan.LoanStatus status);

    @Query("{ 'dueDate': { '$lt': ?0 }, 'status': 'ACTIVE' }")
    List<Loan> findByDueDateBeforeAndStatus(LocalDate currentDate, Loan.LoanStatus status);
}