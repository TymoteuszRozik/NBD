package com.library.repository;

import com.library.config.HibernateConfig;
import com.library.model.Loan;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;

public class LoanRepository {

    public Loan save(Loan loan) {
        Session session = HibernateConfig.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.persist(loan);
            transaction.commit();
            return loan;
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }

    public List<Loan> findActiveLoansByUserId(Long userId) {
        Session session = HibernateConfig.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            List<Loan> loans = session.createQuery(
                            "SELECT l FROM Loan l WHERE l.user.id = :userId AND l.returnDate IS NULL",
                            Loan.class)
                    .setParameter("userId", userId)
                    .list();
            transaction.commit();
            return loans;
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }
}