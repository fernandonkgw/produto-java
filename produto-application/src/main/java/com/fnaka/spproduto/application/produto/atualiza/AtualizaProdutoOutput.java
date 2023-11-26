package com.fnaka.spproduto.application.produto.atualiza;

import com.fnaka.spproduto.domain.produto.Produto;
import com.fnaka.spproduto.domain.produto.ProdutoID;

public record AtualizaProdutoOutput(
        String id
) {

    public static AtualizaProdutoOutput from(final Produto produto) {
        return from(produto.getId());
    }

    public static AtualizaProdutoOutput from(final ProdutoID id) {
        return new AtualizaProdutoOutput(id.getValue());
    }
}
