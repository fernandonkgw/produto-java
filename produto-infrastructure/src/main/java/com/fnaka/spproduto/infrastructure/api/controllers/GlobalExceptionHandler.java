package com.fnaka.spproduto.infrastructure.api.controllers;

import com.fnaka.spproduto.domain.exceptions.DomainException;
import com.fnaka.spproduto.domain.exceptions.NotFoundException;
import com.fnaka.spproduto.domain.validation.DomainError;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;
import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    public GlobalExceptionHandler(final MessageSource messageSource) {
        this.messageSource = Objects.requireNonNull(messageSource);
    }

    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<?> handlerNotFoundException(final NotFoundException ex) {
        final var message = ex.getMessage();
        final var errors = ex.getErrors().stream().map(this::from).toList();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiError.with(message, errors));
    }

    @ExceptionHandler(value = DomainException.class)
    public ResponseEntity<?> handlerDomainException(final DomainException ex) {
        final var message = ex.getMessage();
        final var errors = ex.getErrors().stream().map(this::from).toList();
        return ResponseEntity.badRequest().body(ApiError.with(message, errors));
    }

    private ApiErrorMessage from(DomainError domainError) {
        final var code = domainError.code().getCode();
        final var message = this.messageSource.getMessage(code, domainError.args(), new Locale("pt", "BR"));
        return new ApiErrorMessage(message);
    }

}
