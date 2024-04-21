package com.hackathon.transactionservice.model;

import com.hackathon.transactionservice.enums.Type;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Valid
@Builder
public class Transaction {

    @NotBlank
    private String accountNumber;
    @PositiveOrZero
    private Double transactionAmount;
    private OffsetDateTime transactionDate;
    private Type transactionType;
}
