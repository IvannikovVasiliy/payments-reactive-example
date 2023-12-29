package com.neoflex.payments.controller;

import com.neoflex.payments.domain.dto.PaymentRequestDto;
import com.neoflex.payments.domain.dto.PaymentResponseDto;
import com.neoflex.payments.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping
    public Flux<PaymentResponseDto> getPayments() {
        return paymentService.getPayments();
    }
}
