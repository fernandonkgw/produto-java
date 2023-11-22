package com.fnaka.spproduto.application.produto.busca;

import com.fnaka.spproduto.domain.produto.Produto;

import java.time.Instant;

public record ProdutoOutput(
        String id,
        String nome,
        int preco,
        boolean estaAtivo,
        Instant criadoEm,
        Instant atualizadoEm,
        Instant removidoEm
) {
    public static ProdutoOutput from(final Produto produto) {
        return new ProdutoOutput(
                produto.getId().getValue(),
                produto.getNome(),
                produto.getPreco(),
                produto.isEstaAtivo(),
                produto.getCriadoEm(),
                produto.getAtualizadoEm(),
                produto.getRemovidoEm()
        );
    }
}
