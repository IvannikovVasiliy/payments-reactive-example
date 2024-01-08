package com.neoflex.payments.handler;

import com.neoflex.payments.exception.ResourceAlreadyExistsException;
import com.neoflex.payments.exception.ResourceNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.HashMap;
import java.util.Map;

@Component
public class GlobalErrorHandler extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        Map<String, Object> map = new HashMap<>();
        Throwable throwable = getError(request);
        if (throwable instanceof ConstraintViolationException) {
            map.put("status", HttpStatus.BAD_REQUEST.value());
        } else if (throwable instanceof ResourceNotFoundException) {
            map.put("status", HttpStatus.NOT_FOUND.value());
        } else if (throwable instanceof ResourceAlreadyExistsException) {
            map.put("status", HttpStatus.CONFLICT.value());
        } else {
            map.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        map.put("message", throwable.getMessage());

        return map;
    }
}
