package com.neoflex.payments.controller;

import com.neoflex.payments.domain.dto.PaymentRequestDto;
import com.neoflex.payments.domain.dto.PaymentResponseDto;
import com.neoflex.payments.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;
//
//    @GetMapping(value = "/{id}", consumes = MediaType.TEXT_PLAIN_VALUE)
//    public Mono<ServerResponse> getPaymentById(@PathVariable Long id) {
//        return ServerResponse
//                .ok()
//                .contentType(MediaType.TEXT_HTML)
//                .bodyValue(1);
//    }


//    @GetMapping(value = "/{id}")
//    public Mono<PaymentResponseDto> getPaymentById(@PathVariable Long id) {
//        return paymentService.getPaymentById(id);
//    }
//
//    @GetMapping
//    public Flux<PaymentResponseDto> getPayments() {
//        return paymentService.getPayments();
//    }
}
