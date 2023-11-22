package com.fnaka.spproduto.application.produto.atualiza;

import com.fnaka.spproduto.domain.produto.Produto;

public record AtualizaProdutoOutput(
        String id
) {

    public static AtualizaProdutoOutput from(final Produto produto) {
        return new AtualizaProdutoOutput(produto.getId().getValue());
    }
}
