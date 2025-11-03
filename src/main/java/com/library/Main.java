package com.library;

import com.library.config.HibernateConfig;
import com.library.repository.LibraryItemRepository;

public class Main {
    public static void main(String[] args) {
        try {
            System.out.println("=== Library Management System Starting ===");

            // Initialize sample data FIRST
            initializeSampleData();

            // Then test the repository
            LibraryItemRepository repository = new LibraryItemRepository();
            var items = repository.findAll();

            System.out.println("Application started successfully!");
            System.out.println("Found " + items.size() + " library items:");

            // Display the items
            for (var item : items) {
                if (item instanceof com.library.model.Book book) {
                    System.out.println(" - Book: " + book.getTitle() +
                            " (" + book.getPublicationYear() + ")");
                } else if (item instanceof com.library.model.Magazine magazine) {
                    System.out.println(" - Magazine: " + magazine.getTitle() +
                            " Issue #" + magazine.getIssueNumber() +
                            " (" + magazine.getPublicationYear() + ")");
                }
            }

        } catch (Exception e) {
            System.err.println("❌ Application failed: " + e.getMessage());
            e.printStackTrace();
        } finally {
            HibernateConfig.shutdown();
            System.out.println("Hibernate session factory closed.");
        }
    }

    private static void initializeSampleData() {
        org.hibernate.Session session = HibernateConfig.getSessionFactory().openSession();
        org.hibernate.Transaction transaction = session.beginTransaction();

        try {
            System.out.println("Initializing sample data...");

            com.library.model.Author author1 = new com.library.model.Author("Adam Mickiewicz", "Polish");
            com.library.model.Author author2 = new com.library.model.Author("Stephen King", "American");
            session.persist(author1);
            session.persist(author2);

            com.library.model.Book book1 = new com.library.model.Book("Pan Tadeusz", 1834, "978-83-08-06008-7", "Wydawnictwo Literackie");
            book1.setAvailable(true);
            book1.addAuthor(author1);
            session.persist(book1);

            com.library.model.Book book2 = new com.library.model.Book("To", 1986, "978-83-08-05376-8", "Albatros");
            book2.setAvailable(true);
            book2.addAuthor(author2);
            session.persist(book2);

            com.library.model.Magazine magazine = new com.library.model.Magazine("National Geographic", 2024, 245);
            magazine.setAvailable(true);
            session.persist(magazine);

            transaction.commit();
            System.out.println("✅ Sample data created: 2 books, 1 magazine, 2 authors");

        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.err.println("❌ Failed to create sample data: " + e.getMessage());
            throw e;
        } finally {
            session.close();
        }
    }
}