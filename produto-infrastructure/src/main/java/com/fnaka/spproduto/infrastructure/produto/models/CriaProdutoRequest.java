package com.fnaka.spproduto.infrastructure.produto.models;

public record CriaProdutoRequest(
        String nome,
        Integer preco,
        Boolean estaAtivo
) {
}
