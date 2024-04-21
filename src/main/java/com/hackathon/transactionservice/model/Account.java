package com.hackathon.transactionservice.model;

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
public class Account{

    @NotBlank
    private String accountNumber;
    @NotBlank
    private String name;
    @PositiveOrZero
    private Double balance;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
