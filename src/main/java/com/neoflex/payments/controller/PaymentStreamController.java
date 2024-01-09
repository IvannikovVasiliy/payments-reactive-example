package com.neoflex.payments.controller;

import com.neoflex.payments.dto.PaymentRequestDto;
import com.neoflex.payments.dto.PaymentResponseDto;
import com.neoflex.payments.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v3/payments")
public class PaymentStreamController {

    private final PaymentService paymentService;

    @PostMapping
    public Mono<Void> addPayments(@RequestBody Flux<PaymentRequestDto> paymentsRequestFlux) {
        return paymentService.addPaymentsFlux(paymentsRequestFlux);
    }
}
