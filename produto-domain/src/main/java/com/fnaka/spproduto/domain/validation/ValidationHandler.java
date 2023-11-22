package com.fnaka.spproduto.domain.validation;

import java.util.List;

public interface ValidationHandler {

    ValidationHandler append(ErrorCode errorCode, Object... args);
    ValidationHandler append(DomainError domainError);

    ValidationHandler append(ValidationHandler handler);

    <T> T validate(Validation<T> aValidation);

    List<DomainError> getErrors();

    default boolean hasErrors() {
        return getErrors() != null && !getErrors().isEmpty();
    }

    default DomainError firstError() {
        if (getErrors() != null && !getErrors().isEmpty()) {
            return getErrors().get(0);
        } else {
            return null;
        }
    }

    interface Validation<T> {
        T validate();
    }
}
