package com.fnaka.spproduto.infrastructure.produto.models;

import com.fnaka.spproduto.application.produto.atualiza.AtualizaProdutoOutput;

public record AtualizaProdutoResponse(
        String id
) {

    public static AtualizaProdutoResponse from(final AtualizaProdutoOutput output) {
        return new AtualizaProdutoResponse(
                output.id()
        );
    }
}
