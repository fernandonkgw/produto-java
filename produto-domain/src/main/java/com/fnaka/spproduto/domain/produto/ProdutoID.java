package com.fnaka.spproduto.domain.produto;

import com.fnaka.spproduto.domain.Identifier;

import java.util.Objects;
import java.util.UUID;

public class ProdutoID extends Identifier {

    private final String value;

    public ProdutoID(final String value) {
        this.value = Objects.requireNonNull(value);
    }

    public static ProdutoID unique() {
        return ProdutoID.from(UUID.randomUUID());
    }

    public static ProdutoID from(final String anId) {
        return new ProdutoID(anId);
    }

    public static ProdutoID from(final UUID anId) {
        return new ProdutoID(anId.toString().toLowerCase());
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final ProdutoID produtoID = (ProdutoID) o;
        return Objects.equals(getValue(), produtoID.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }
}
