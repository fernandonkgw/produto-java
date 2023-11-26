package com.fnaka.spproduto.infrastructure.produto.models;

public record AtualizaProdutoRequest(
        String nome,
        Integer preco,
        Boolean estaAtivo
) {
}
