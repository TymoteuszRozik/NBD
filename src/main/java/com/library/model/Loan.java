package com.library.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "loans")
public class Loan {

    @Id
    private String id;

    @NotNull(message = "User ID is required")
    @Indexed
    @Field("user_id")
    private String userId;

    @NotNull(message = "Item ID is required")
    @Indexed
    @Field("item_id")
    private String itemId;

    @Field("item_title")
    private String itemTitle;

    @Field("item_type")
    private String itemType;

    @NotNull(message = "Loan date is required")
    @Field("loan_date")
    private LocalDate loanDate;

    @NotNull(message = "Due date is required")
    @Field("due_date")
    private LocalDate dueDate;

    @Field("return_date")
    private LocalDate returnDate;

    @Field("status")
    private LoanStatus status;

    @Version
    private Long version;

    public enum LoanStatus {
        ACTIVE, RETURNED, OVERDUE
    }
}