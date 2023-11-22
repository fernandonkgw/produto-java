package com.fnaka.spproduto.infrastructure.api.controllers;

import java.util.List;

public record ApiError(String message, List<ApiErrorMessage> errors) {

    public static ApiError with(String message, List<ApiErrorMessage> errors) {
        return new ApiError(message, errors);
    }
}
