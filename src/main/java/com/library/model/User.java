package com.library.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "persons")
public class User extends Person {

    @Field("reader_card_number")
    private String readerCardNumber;

    @Field("active_loans_count")
    private Integer activeLoansCount = 0;

    @Field("max_allowed_loans")
    private Integer maxAllowedLoans = 5;

    private List<EmbeddedLoan> currentLoans = new ArrayList<>();

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmbeddedLoan {
        private String loanId;
        private String itemId;
        private String itemTitle;
        private String loanDate;
        private String dueDate;
    }
}