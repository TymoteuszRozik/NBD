package com.library.service;

import com.library.model.Loan;
import com.library.model.LibraryItem;
import com.library.model.User;
import com.library.repository.LibraryItemRepository;
import com.library.repository.LoanRepository;
import org.hibernate.Session;
import org.hibernate.Transaction;
import com.library.config.HibernateConfig;
import java.time.LocalDate;

public class LoanService {

    private final LibraryItemRepository itemRepository;
    private final LoanRepository loanRepository;

    public LoanService() {
        this.itemRepository = new LibraryItemRepository();
        this.loanRepository = new LoanRepository();
    }

    public Loan borrowItem(Long userId, Long itemId, LocalDate dueDate) {
        Session session = HibernateConfig.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();

        try {
            session.createNativeQuery("SET TRANSACTION ISOLATION LEVEL READ COMMITTED").executeUpdate();

            LibraryItem item = itemRepository.findByIdWithPessimisticLock(itemId)
                    .orElseThrow(() -> new RuntimeException("Item not found"));

            if (!item.getAvailable()) {
                throw new RuntimeException("Item is not available for loan");
            }

            item.setAvailable(false);
            itemRepository.update(item);

            User user = session.get(User.class, userId);
            Loan loan = new Loan(user, item, LocalDate.now(), dueDate);
            loanRepository.save(loan);

            transaction.commit();
            return loan;

        } catch (Exception e) {
            transaction.rollback();
            throw new RuntimeException("Failed to borrow item: " + e.getMessage(), e);
        }
    }

    public void returnItem(Long loanId) {
        Session session = HibernateConfig.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();

        try {
            Loan loan = session.get(Loan.class, loanId);
            if (loan == null) {
                throw new RuntimeException("Loan not found");
            }

            LibraryItem item = loan.getItem();
            item.setAvailable(true);
            loan.setReturnDate(LocalDate.now());

            session.merge(item);
            session.merge(loan);

            transaction.commit();

        } catch (Exception e) {
            transaction.rollback();
            throw new RuntimeException("Failed to return item: " + e.getMessage(), e);
        }
    }
}