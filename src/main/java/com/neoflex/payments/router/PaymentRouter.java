package com.neoflex.payments.router;

import com.neoflex.payments.dto.PaymentRequestDto;
import com.neoflex.payments.dto.PaymentResponseDto;
import com.neoflex.payments.dto.UpdatePaymentRequestDto;
import com.neoflex.payments.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
@RequiredArgsConstructor
public class PaymentRouter {

    private final PaymentService paymentService;

    @Bean
    public RouterFunction<ServerResponse> handle() {
        return RouterFunctions
                .route(
                        RequestPredicates
                                .GET("/v2/payments/{id}")
                                .and(RequestPredicates.accept(MediaType.TEXT_PLAIN)),
                        serverRequest -> ServerResponse
                                .ok()
                                .headers(headers -> headers.add("Correlation-Id", serverRequest.pathVariable("id")))
                                .body(
                                        paymentService.getPaymentById(Long.valueOf(serverRequest.pathVariable("id"))),
                                        PaymentResponseDto.class
                                )

                )
                .andRoute(
                        RequestPredicates
                                .PUT("/v2/payments/{id}"),
                        serverRequest -> {
                            Long idPayment = Long.valueOf(serverRequest.pathVariable("id"));
                            return serverRequest
                                    .bodyToMono(UpdatePaymentRequestDto.class)
                                    .flatMap(payment -> ServerResponse
                                            .status(HttpStatus.NO_CONTENT)
                                            .header("Correlation-Id", idPayment.toString())
                                            .body(paymentService.updatePayment(idPayment, payment), Void.class));
                        }
                )
                .andRoute(
                        RequestPredicates
                                .DELETE("/v2/payments/{id}"),
                        serverRequest -> {
                            Long idPayment = Long.valueOf(serverRequest.pathVariable("id"));
                            return ServerResponse
                                    .status(HttpStatus.NO_CONTENT)
                                    .header("Correlation-Id", idPayment.toString())
                                    .body(paymentService.deletePaymentById(idPayment), Void.class);
                        }
                );
    }

    @Bean
    public RouterFunction<ServerResponse> handleAddPayment() {
        return RouterFunctions
                .route(
                        RequestPredicates
                                .POST("/v2/payments")
                                .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
                        serverRequest -> serverRequest
                                .bodyToMono(PaymentRequestDto.class)
                                .flatMap(payment -> ServerResponse
                                        .status(HttpStatus.CREATED)
                                        .header("Correlation-Id", payment.getId().toString())
                                        .body(paymentService.addPayment(payment), String.class))
                );
    }

}
