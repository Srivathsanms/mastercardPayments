package com.evaluation.mastercardPayments.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferRequestDto {

    @NotNull
    @NotBlank
    private String debtorAccount;

    @NotNull
    @NotBlank
    private String creditorAccount;

    @NotNull
    private BigDecimal amount;

    @NotNull
    @NotBlank
    @Size(min = 1, max = 3)
    private String currency;
}
