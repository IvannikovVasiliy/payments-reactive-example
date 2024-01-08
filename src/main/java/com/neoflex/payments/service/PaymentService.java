package com.neoflex.payments.service;

import com.neoflex.payments.dto.PaymentRequestDto;
import com.neoflex.payments.dto.PaymentResponseDto;
import com.neoflex.payments.dto.UpdatePaymentRequestDto;
import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PaymentService {

    Mono<PaymentResponseDto> getPaymentById(Long id);

    Flux<PaymentResponseDto> getPayments();

    Mono<Void> addPayment(@Valid PaymentRequestDto paymentRequestDto);

    Mono<Void> updatePayment(Long id, @Valid UpdatePaymentRequestDto updatePaymentRequestDto);

    Mono<Void> deletePaymentById(Long id);
}
