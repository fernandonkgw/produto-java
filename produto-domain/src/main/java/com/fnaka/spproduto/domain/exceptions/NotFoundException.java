package com.fnaka.spproduto.domain.exceptions;

import com.fnaka.spproduto.domain.AggregateRoot;
import com.fnaka.spproduto.domain.Identifier;
import com.fnaka.spproduto.domain.validation.DomainError;
import com.fnaka.spproduto.domain.validation.ErrorCode;

import java.util.Collections;
import java.util.List;

public class NotFoundException extends DomainException {

    protected NotFoundException(
            final String aMessage,
            final List<DomainError> errors
    ) {
        super(aMessage, errors);
    }

    public static NotFoundException with(
            final Class<? extends AggregateRoot> agregado,
            final Identifier id
    ) {
//        final var anError = "%s com ID %s nao encontrado".formatted(
//                agregado.getSimpleName(),
//                id.getValue()
//        );
        final var domainError = DomainError.with(ErrorCode.PRO_006, agregado.getSimpleName(), id.getValue());
        return new NotFoundException("", List.of(domainError));
    }
}
