package com.fnaka.spproduto.application.produto.cria;

import com.fnaka.spproduto.domain.produto.Produto;
import com.fnaka.spproduto.domain.produto.ProdutoID;

public record CriaProdutoOutput(
        String id
) {
    public static CriaProdutoOutput from(final ProdutoID produtoID) {
        return new CriaProdutoOutput(produtoID.getValue());
    }

    public static CriaProdutoOutput from(final Produto produto) {
        return from(produto.getId());
    }
}
