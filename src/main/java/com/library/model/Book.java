package com.library.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Book extends LibraryItem {

    private String isbn;
    private String publisher;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "book_authors",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private List<Author> authors = new ArrayList<>();

    public Book() {}

    public Book(String title, Integer publicationYear, String isbn, String publisher) {
        super(title, publicationYear);
        this.isbn = isbn;
        this.publisher = publisher;
    }

    // getters and setters
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public String getPublisher() { return publisher; }
    public void setPublisher(String publisher) { this.publisher = publisher; }
    public List<Author> getAuthors() { return authors; }
    public void setAuthors(List<Author> authors) { this.authors = authors; }

    public void addAuthor(Author author) {
        authors.add(author);
        author.getBooks().add(this);
    }
}