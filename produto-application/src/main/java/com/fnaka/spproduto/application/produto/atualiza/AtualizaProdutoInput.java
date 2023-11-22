package com.fnaka.spproduto.application.produto.atualiza;

public record AtualizaProdutoInput(
        String id,
        String nome,
        Integer preco,
        Boolean estaAtivo
) {
    public static AtualizaProdutoInput with(
            final String id,
            final String nome,
            final Integer preco,
            final Boolean estaAtivo
    ) {
        return new AtualizaProdutoInput(
                id,
                nome,
                preco,
                estaAtivo != null ? estaAtivo : true
        );
    }
}
