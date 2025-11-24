package com.library.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "library_items")
public abstract class LibraryItem {

    @Id
    private String id;

    @NotBlank(message = "Title is required")
    @Indexed
    private String title;

    @NotNull(message = "Publication year is required")
    @Field("publication_year")
    private Integer publicationYear;

    @Field("available")
    private Boolean available = true;

    @Version
    private Long version;

    @Field("item_type")
    private String itemType;

    @Field("current_loan_id")
    private String currentLoanId;
}