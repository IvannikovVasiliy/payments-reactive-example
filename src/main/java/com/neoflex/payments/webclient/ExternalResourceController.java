package com.neoflex.payments.webclient;

import com.neoflex.payments.dto.PaymentResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/external/payments")
public class ExternalResourceController {

    @Value("${app.hostPort}")
    private String HOST_PORT;

    @GetMapping
    public Flux<PaymentResponseDto> getAllPayments() {
        return WebClient
                .create("http://localhost:8080/v1/payments")
                .get()
                .retrieve()
                .bodyToFlux(PaymentResponseDto.class);
    }

    @GetMapping("/{id}")
    public Mono<PaymentResponseDto> getPaymentById(@PathVariable Long id) {
        return WebClient
                .create(HOST_PORT)
                .get()
                .uri("/v2/payments/" + id)
                .retrieve()
                .bodyToMono(PaymentResponseDto.class)
                .doOnError(err -> {
                    System.out.println();
                });

    }

//    return WebClient
//            .create(HOST_PORT)
//            .get()
//                .uri("/v2/payments/" + id)
//                .exchangeToMono(clientResponse -> {
//        log.debug("The response with correlationId={}", clientResponse.headers().header("Correlation-Id"));
//        return clientResponse.bodyToMono(PaymentResponseDto.class);
//    });
}
