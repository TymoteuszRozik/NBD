package com.library.config;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;

import java.util.Properties;

public class HibernateConfig {
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            Configuration configuration = new Configuration();

            Properties settings = new Properties();
            settings.put(Environment.DRIVER, "org.postgresql.Driver");
            settings.put(Environment.URL, "jdbc:postgresql://localhost:5432/nbddb");
            settings.put(Environment.USER, "nbd");
            settings.put(Environment.PASS, "nbdpassword");
            settings.put(Environment.DIALECT, "org.hibernate.dialect.PostgreSQLDialect");
            settings.put(Environment.SHOW_SQL, "true");
            settings.put(Environment.HBM2DDL_AUTO, "create");
            settings.put(Environment.FORMAT_SQL, "true");

            settings.put("hibernate.physical_naming_strategy", "org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy");
            settings.put("hibernate.implicit_naming_strategy", "org.hibernate.boot.model.naming.ImplicitNamingStrategyJpaCompliantImpl");

            configuration.setProperties(settings);

            configuration.addAnnotatedClass(com.library.model.Person.class);
            configuration.addAnnotatedClass(com.library.model.User.class);
            configuration.addAnnotatedClass(com.library.model.Librarian.class);
            configuration.addAnnotatedClass(com.library.model.LibraryItem.class);
            configuration.addAnnotatedClass(com.library.model.Book.class);
            configuration.addAnnotatedClass(com.library.model.Magazine.class);
            configuration.addAnnotatedClass(com.library.model.Author.class);
            configuration.addAnnotatedClass(com.library.model.Loan.class);

            return configuration.buildSessionFactory();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ExceptionInInitializerError(e);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        getSessionFactory().close();
    }
}