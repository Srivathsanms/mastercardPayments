package com.evaluation.mastercardPayments.dto;

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
public class AccountRequestDto {

    @NotNull
    @NotBlank
    @Size(min = 3, max = 3)
    private String accountId;

    @NotNull
    private BigDecimal balance;

    @NotNull
    @Size(min = 1, max = 3)
    private String currency;
}
