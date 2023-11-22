package com.fnaka.spproduto.application.produto.lista;

import com.fnaka.spproduto.domain.produto.Produto;

import java.time.Instant;

public record ListaProdutoOutput(
        String id,
        String nome,
        int preco,
        boolean estaAtivo,
        Instant criadoEm,
        Instant atualizadoEm
) {
    public static ListaProdutoOutput from(final Produto produto) {
        return new ListaProdutoOutput(
                produto.getId().getValue(),
                produto.getNome(),
                produto.getPreco(),
                produto.isEstaAtivo(),
                produto.getCriadoEm(),
                produto.getAtualizadoEm()
        );
    }
}
