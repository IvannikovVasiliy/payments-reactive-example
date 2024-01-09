package com.neoflex.payments.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neoflex.payments.dto.PaymentRequestDto;
import com.neoflex.payments.dto.PaymentResponseDto;
import com.neoflex.payments.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
@EnableScheduling
public class PaymentConsumer {

    private final Consumer<byte[], byte[]> consumer;
    private final ObjectMapper objectMapper;
    private final PaymentService paymentService;

    @Scheduled(fixedRate = 500)
    public void pollMessages() {
        List<PaymentRequestDto> consumeMessagesList = new ArrayList<>();
        Mono
                .fromRunnable(() -> {
                    ConsumerRecords<byte[], byte[]> consumerRecords = consumer.poll(Duration.ofMillis(50));
                    consumerRecords.forEach(payment -> {
                        try {
                            PaymentRequestDto paymentRequestDto =
                                    objectMapper.readValue(payment.value(), PaymentRequestDto.class);
                            consumeMessagesList.add(paymentRequestDto);
                        } catch (IOException e) {
                            log.error("Unable to parse");
                        }
                    });
                })
                .doOnSuccess(success -> {
                    if (!consumeMessagesList.isEmpty()) {
                        Flux<PaymentRequestDto> flux = Flux.fromIterable(consumeMessagesList);
                        WebClient
                                .create("http://localhost:8080/v3/payments")
                                .post()
                                .body(flux, Flux.class)
                                .retrieve()
                                .bodyToMono(Void.class)
                                .subscribe();
                    }
                })
                .subscribe();
    }
}