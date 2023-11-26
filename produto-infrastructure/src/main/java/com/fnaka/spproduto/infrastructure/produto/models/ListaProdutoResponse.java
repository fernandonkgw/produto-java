package com.fnaka.spproduto.infrastructure.produto.models;

import com.fnaka.spproduto.application.produto.lista.ListaProdutoOutput;

import java.time.Instant;

public record ListaProdutoResponse(
        String id,
        String nome,
        int preco,
        Instant criadoEm
) {

    public static ListaProdutoResponse from(final ListaProdutoOutput output) {
        return new ListaProdutoResponse(
                output.id(),
                output.nome(),
                output.preco(),
                output.criadoEm()
        );
    }
}
