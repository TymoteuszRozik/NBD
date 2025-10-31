package com.library.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class Magazine extends LibraryItem {

    @Column(name = "issue_number")
    private Integer issueNumber;

    public Magazine() {}

    public Magazine(String title, Integer publicationYear, Integer issueNumber) {
        super(title, publicationYear);
        this.issueNumber = issueNumber;
    }

    // getters and setters
    public Integer getIssueNumber() { return issueNumber; }
    public void setIssueNumber(Integer issueNumber) { this.issueNumber = issueNumber; }
}