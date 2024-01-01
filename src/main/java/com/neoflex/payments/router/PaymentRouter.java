package com.neoflex.payments.router;

import com.neoflex.payments.domain.dto.PaymentResponseDto;
import com.neoflex.payments.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class PaymentRouter {

    private final PaymentService paymentService;

    @Bean
    public RouterFunction<ServerResponse> handle() {
        return RouterFunctions
                .route(
                        RequestPredicates
                                .GET("/payments/{id}")
                                .and(RequestPredicates.accept(MediaType.TEXT_PLAIN)),
                        serverRequest -> ServerResponse
                                .ok()
                                .header("correlationId", serverRequest.pathVariable("id"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(
                                        paymentService.getPaymentById(Long.valueOf(serverRequest.pathVariable("id"))),
                                        PaymentResponseDto.class
                                )
                );
    }
}
