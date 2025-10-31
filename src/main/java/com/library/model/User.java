package com.library.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "library_user")
public class User extends Person {

    @Column(name = "reader_card_number", unique = true)
    private String readerCardNumber;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Loan> loans = new ArrayList<>();

    public User() {}

    public User(String firstName, String lastName, String email, String readerCardNumber) {
        super(firstName, lastName, email);
        this.readerCardNumber = readerCardNumber;
    }

    // getters and setters
    public String getReaderCardNumber() { return readerCardNumber; }
    public void setReaderCardNumber(String readerCardNumber) { this.readerCardNumber = readerCardNumber; }
    public List<Loan> getLoans() { return loans; }
    public void setLoans(List<Loan> loans) { this.loans = loans; }

    public void addLoan(Loan loan) {
        loans.add(loan);
        loan.setUser(this);
    }
}