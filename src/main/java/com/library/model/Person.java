package com.library.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "persons")
public abstract class Person {

    @Id
    private String id;

    @NotBlank(message = "First name is required")
    @Field("first_name")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Field("last_name")
    private String lastName;

    @Email(message = "Email should be valid")
    private String email;

    @Field("person_type")
    private String personType;
}