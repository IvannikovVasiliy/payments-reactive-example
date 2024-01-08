package com.neoflex.payments.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@NotNull(message = "not null")
public class PaymentRequestDto {

    private Long id;

    @Size(min = 6)
    private String cardNumber;

    private LocalDateTime date;
}
