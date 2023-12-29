package com.neoflex.payments.service;

import com.neoflex.payments.domain.dto.PaymentResponseDto;
import reactor.core.publisher.Flux;

public interface PaymentService {
    Flux<PaymentResponseDto> getPayments();
}
