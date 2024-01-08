package com.neoflex.payments.service.impl;

import com.neoflex.payments.dto.PaymentRequestDto;
import com.neoflex.payments.dto.PaymentResponseDto;
import com.neoflex.payments.dto.UpdatePaymentRequestDto;
import com.neoflex.payments.exception.ResourceAlreadyExistsException;
import com.neoflex.payments.exception.ResourceNotFoundException;
import com.neoflex.payments.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Validated
public class PaymentServiceImpl implements PaymentService {

    private final DatabaseClient databaseClient;

    @Override
    public Mono<PaymentResponseDto> getPaymentById(Long id) {
        return databaseClient
                .sql("select * from payments where id=:id")
                .bind("id", id)
                .fetch()
                .one()
                .flatMap(paymentMap -> {
                    PaymentResponseDto paymentResponseDto = new PaymentResponseDto(
                            (Long) paymentMap.get("id"),
                            (String) paymentMap.get("card_number"),
                            (LocalDateTime) paymentMap.get("date")
                    );
                    return Mono.just(paymentResponseDto);
                })
                .switchIfEmpty(Mono.error(
                        new ResourceNotFoundException(String.format("Payment with id=%s not found", id))
                ));
    }

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

    @Override
    public Mono<Void> addPayment(PaymentRequestDto paymentRequestDto) {
        return databaseClient
                .sql("insert into payments(id, card_number, date) values(:id, :card_number, :date);")
                .bind("id", paymentRequestDto.getId())
                .bind("card_number", paymentRequestDto.getCardNumber())
                .bind("date", paymentRequestDto.getDate())
                .then()
                .onErrorResume(throwable -> {
                    if (throwable instanceof DuplicateKeyException) {
                        return Mono.error(new ResourceAlreadyExistsException(
                                String.format("Payment with id=%d already exists", paymentRequestDto.getId())
                        ));
                    }
                    return Mono.error(throwable);
                });
    }

    @Override
    public Mono<Void> updatePayment(Long id, UpdatePaymentRequestDto paymentRequestDto) {
        return databaseClient
                .sql("update payments set card_number=:card_number, date=:date where id=:id")
                .bind("id", id)
                .bind("card_number", paymentRequestDto.getCardNumber())
                .bind("date", paymentRequestDto.getDate())
                .fetch()
                .rowsUpdated()
                .flatMap(count -> {
                    if (count > 0) {
                        return Mono.empty();
                    } else {
                        return Mono.error(new ResourceNotFoundException(
                                String.format("Payment with id=%s not found", id)
                        ));
                    }
                });
    }

    @Override
    public Mono<Void> deletePaymentById(Long id) {
        return databaseClient
                .sql("delete from payments where id=:id")
                .bind("id", id)
                .fetch()
                .rowsUpdated()
                .flatMap(count -> {
                    if (count > 0) {
                        return Mono.empty();
                    } else {
                        return Mono.error(
                                new ResourceNotFoundException(String.format("Payment with id=%s not found", id))
                        );
                    }
                });
    }
}
