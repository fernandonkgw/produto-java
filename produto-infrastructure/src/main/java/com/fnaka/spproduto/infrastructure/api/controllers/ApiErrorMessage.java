package com.fnaka.spproduto.infrastructure.api.controllers;

import com.fnaka.spproduto.domain.validation.DomainError;

public record ApiErrorMessage(String message) {

    public static ApiErrorMessage from(DomainError domainError) {
        return new ApiErrorMessage(domainError.message());
    }
}
