package com.neoflex.payments.service.impl;

import com.neoflex.payments.domain.dto.PaymentResponseDto;
import com.neoflex.payments.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoOperator;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final DatabaseClient databaseClient;

    @Override
    public Flux<PaymentResponseDto> getPayments() {
        return databaseClient
                .sql("select * from payments")
                .fetch()
                .all()
                .flatMap(paymentMap -> {
                    PaymentResponseDto paymentResponseDto = new PaymentResponseDto(
                            (Long) paymentMap.get("id"),
                            (String) paymentMap.get("card_number"),
                            (LocalDateTime) paymentMap.get("date")
                    );
                    return Mono.just(paymentResponseDto);
                });
    }
}
