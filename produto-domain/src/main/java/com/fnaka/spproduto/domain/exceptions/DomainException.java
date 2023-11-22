package com.fnaka.spproduto.domain.exceptions;

import com.fnaka.spproduto.domain.validation.DomainError;

import java.util.List;

public class DomainException extends NoStacktraceException {

    protected final List<DomainError> errors;

    protected DomainException(final String aMessage, final List<DomainError> errors) {
        super(aMessage);
        this.errors = errors;
    }

    public List<DomainError> getErrors() {
        return this.errors;
    }
}
