package com.fnaka.spproduto.application.produto.cria;

public record CriaProdutoInput(
        String nome,
        Integer preco,
        boolean estaAtivo
) {
    public static CriaProdutoInput with(
            final String nome,
            final Integer preco,
            final Boolean estaAtivo
    ) {
        return new CriaProdutoInput(
                nome,
                preco,
                estaAtivo != null ? estaAtivo : true
        );
    }
}
