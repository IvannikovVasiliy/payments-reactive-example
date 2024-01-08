package com.neoflex.payments.advice;

import com.neoflex.payments.dto.MessageDto;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

import java.util.Date;

@RestControllerAdvice
public class GlobalHandler {

    @ExceptionHandler(RuntimeException.class)
    public Mono<MessageDto> handleResourceAlreadyExists(RuntimeException e) {
        MessageDto messageDto = new MessageDto(e.getMessage(), new Date());
        return Mono.just(messageDto);
    }
}
