package com.fnaka.spproduto.domain.validation.handler;

import com.fnaka.spproduto.domain.validation.ErrorCode;
import com.fnaka.spproduto.domain.validation.DomainError;
import com.fnaka.spproduto.domain.validation.ValidationHandler;

import java.util.ArrayList;
import java.util.List;

public class Notification implements ValidationHandler {

    private final List<DomainError> errors;

    private Notification(final List<DomainError> errors) {
        this.errors = errors;
    }

    public static Notification create() {
        return new Notification(new ArrayList<>());
    }

    public static Notification create(final ErrorCode errorCode, final Object... args) {
        return new Notification(new ArrayList<>())
                .append(DomainError.with(errorCode, args));
    }

    @Override
    public Notification append(final ErrorCode errorCode, final Object... args) {
        this.errors.add(DomainError.with(errorCode, args));
        return this;
    }

    @Override
    public Notification append(final DomainError domainError) {
        this.errors.add(domainError);
        return this;
    }

    @Override
    public Notification append(ValidationHandler handler) {
        this.errors.addAll(handler.getErrors());
        return this;
    }

    @Override
    public <T> T validate(Validation<T> aValidation) {
        return aValidation.validate();
    }

    @Override
    public List<DomainError> getErrors() {
        return this.errors;
    }
}
