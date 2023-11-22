package com.fnaka.spproduto.infrastructure.produto.models;

import com.fnaka.spproduto.application.produto.busca.ProdutoOutput;

import java.time.Instant;

public record ProdutoResponse(
        String id,
        String nome,
        int preco,
        boolean estaAtivo,
        Instant criadoEm,
        Instant atualizadoEm,
        Instant removidoEm
) {
    public static ProdutoResponse from(final ProdutoOutput output) {
        return new ProdutoResponse(
                output.id(),
                output.nome(),
                output.preco(),
                output.estaAtivo(),
                output.criadoEm(),
                output.atualizadoEm(),
                output.removidoEm()
        );
    }
}
