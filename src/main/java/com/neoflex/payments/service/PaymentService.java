package com.neoflex.payments.service;

import com.neoflex.payments.domain.dto.PaymentResponseDto;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PaymentService {

    Mono<PaymentResponseDto> getPaymentById(Long id);

    Flux<PaymentResponseDto> getPayments();
}
