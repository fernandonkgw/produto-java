package com.fnaka.spproduto.application.produto.lista;

public record ProdutoSearchQuery(
        int page,
        int perPage,
        String termo,
        String sort,
        String direction
) {
}
