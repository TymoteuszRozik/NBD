package com.library.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class Librarian extends Person {

    @Column(name = "employee_id", unique = true)
    private String employeeId;

    public Librarian() {}

    public Librarian(String firstName, String lastName, String email, String employeeId) {
        super(firstName, lastName, email);
        this.employeeId = employeeId;
    }

    // getters and setters
    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }
}