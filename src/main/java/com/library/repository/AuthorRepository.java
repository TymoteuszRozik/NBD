package com.library.repository;

import com.library.config.HibernateConfig;
import com.library.model.Author;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;
import java.util.Optional;

public class AuthorRepository {

    public Author save(Author author) {
        Session session = HibernateConfig.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.persist(author);
            transaction.commit();
            return author;
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }

    public Optional<Author> findById(Long id) {
        Session session = HibernateConfig.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            Author author = session.get(Author.class, id);
            transaction.commit();
            return Optional.ofNullable(author);
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }

    public List<Author> findAll() {
        Session session = HibernateConfig.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            List<Author> authors = session.createQuery("FROM Author", Author.class).list();
            transaction.commit();
            return authors;
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }

    public List<Author> findByName(String name) {
        Session session = HibernateConfig.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            List<Author> authors = session.createQuery(
                            "FROM Author WHERE name LIKE :name", Author.class)
                    .setParameter("name", "%" + name + "%")
                    .list();
            transaction.commit();
            return authors;
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }

    public void update(Author author) {
        Session session = HibernateConfig.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.merge(author);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }

    public void delete(Long id) {
        Session session = HibernateConfig.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            Author author = session.get(Author.class, id);
            if (author != null) {
                session.remove(author);
            }
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }

    public List<Author> findByNationality(String nationality) {
        Session session = HibernateConfig.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            List<Author> authors = session.createQuery(
                            "FROM Author WHERE nationality = :nationality", Author.class)
                    .setParameter("nationality", nationality)
                    .list();
            transaction.commit();
            return authors;
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }
}