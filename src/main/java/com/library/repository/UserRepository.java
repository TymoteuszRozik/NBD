package com.library.repository;

import com.library.config.HibernateConfig;
import com.library.model.User;
import com.library.model.Loan;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;
import java.util.Optional;

public class UserRepository {

    public User save(User user) {
        Session session = HibernateConfig.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.persist(user);
            transaction.commit();
            return user;
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }

    public Optional<User> findById(Long id) {
        Session session = HibernateConfig.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            User user = session.get(User.class, id);
            transaction.commit();
            return Optional.ofNullable(user);
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }

    public Optional<User> findByIdWithLoans(Long id) {
        Session session = HibernateConfig.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            User user = session.createQuery(
                            "SELECT u FROM User u LEFT JOIN FETCH u.loans WHERE u.id = :id", User.class)
                    .setParameter("id", id)
                    .uniqueResult();
            transaction.commit();
            return Optional.ofNullable(user);
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }

    public List<User> findAll() {
        Session session = HibernateConfig.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            List<User> users = session.createQuery("FROM User", User.class).list();
            transaction.commit();
            return users;
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }

    public Optional<User> findByEmail(String email) {
        Session session = HibernateConfig.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            User user = session.createQuery(
                            "FROM User WHERE email = :email", User.class)
                    .setParameter("email", email)
                    .uniqueResult();
            transaction.commit();
            return Optional.ofNullable(user);
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }

    public Optional<User> findByReaderCardNumber(String readerCardNumber) {
        Session session = HibernateConfig.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            User user = session.createQuery(
                            "FROM User WHERE readerCardNumber = :readerCardNumber", User.class)
                    .setParameter("readerCardNumber", readerCardNumber)
                    .uniqueResult();
            transaction.commit();
            return Optional.ofNullable(user);
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }

    public void update(User user) {
        Session session = HibernateConfig.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.merge(user);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }

    public boolean hasActiveLoans(Long userId) {
        Session session = HibernateConfig.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            Long count = session.createQuery(
                            "SELECT COUNT(l) FROM Loan l WHERE l.user.id = :userId AND l.returnDate IS NULL", Long.class)
                    .setParameter("userId", userId)
                    .uniqueResult();
            transaction.commit();
            return count != null && count > 0;
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }

    public int getActiveLoansCount(Long userId) {
        Session session = HibernateConfig.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            Long count = session.createQuery(
                            "SELECT COUNT(l) FROM Loan l WHERE l.user.id = :userId AND l.returnDate IS NULL", Long.class)
                    .setParameter("userId", userId)
                    .uniqueResult();
            transaction.commit();
            return count != null ? count.intValue() : 0;
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }
}