package com.library.repository;

import com.library.config.HibernateConfig;
import com.library.model.LibraryItem;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.LockMode;
import java.util.List;
import java.util.Optional;

public class LibraryItemRepository {

    public LibraryItem save(LibraryItem item) {
        Session session = HibernateConfig.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.persist(item);
            transaction.commit();
            return item;
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    public Optional<LibraryItem> findById(Long id) {
        Session session = HibernateConfig.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        try {
            LibraryItem item = session.get(LibraryItem.class, id);
            transaction.commit();
            return Optional.ofNullable(item);
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    public Optional<LibraryItem> findByIdWithPessimisticLock(Long id) {
        Session session = HibernateConfig.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        try {
            LibraryItem item = session.get(LibraryItem.class, id, LockMode.PESSIMISTIC_WRITE);
            transaction.commit();
            return Optional.ofNullable(item);
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    public List<LibraryItem> findAll() {
        Session session = HibernateConfig.getSessionFactory().openSession();
        try {
            return session.createQuery("FROM LibraryItem", LibraryItem.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            session.close();
        }
    }

    public void update(LibraryItem item) {
        Session session = HibernateConfig.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.merge(item);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        } finally {
            session.close();
        }
    }
}