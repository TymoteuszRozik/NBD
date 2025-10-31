package com.library.model;

import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class LibraryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(name = "publication_year")
    private Integer publicationYear;

    @Column(nullable = false)
    private Boolean available = true;

    @Version
    private Long version;

    public LibraryItem() {}

    public LibraryItem(String title, Integer publicationYear) {
        this.title = title;
        this.publicationYear = publicationYear;
    }

    // getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public Integer getPublicationYear() { return publicationYear; }
    public void setPublicationYear(Integer publicationYear) { this.publicationYear = publicationYear; }
    public Boolean getAvailable() { return available; }
    public void setAvailable(Boolean available) { this.available = available; }
    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }
}