package com.fnaka.spproduto.domain.validation;

public record DomainError(ErrorCode code, Object... args) {

    public static DomainError with(final ErrorCode code, final Object... args) {
        return new DomainError(code, args);
    }

    public String message() {
        return code().getMessage().formatted(args());
    }
}
