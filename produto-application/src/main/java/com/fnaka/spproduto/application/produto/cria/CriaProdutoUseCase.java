package com.fnaka.spproduto.application.produto.cria;

import com.fnaka.spproduto.application.UseCase;
import com.fnaka.spproduto.domain.produto.Produto;
import com.fnaka.spproduto.domain.produto.ProdutoID;

public abstract class CriaProdutoUseCase
        extends UseCase<CriaProdutoUseCase.Input, CriaProdutoUseCase.Output> {

    public record Input(
            String nome,
            Integer preco,
            boolean estaAtivo
    ) {

        public static CriaProdutoUseCase.Input with(
                final String nome,
                final Integer preco,
                final Boolean estaAtivo
        ) {
            return new CriaProdutoUseCase.Input(
                    nome,
                    preco,
                    estaAtivo != null ? estaAtivo : true
            );
        }
    }

    public record Output(String id) {
        public static CriaProdutoUseCase.Output from(final ProdutoID produtoID) {
            return new CriaProdutoUseCase.Output(produtoID.getValue());
        }

        public static CriaProdutoUseCase.Output from(final Produto produto) {
            return from(produto.getId());
        }
    }
}
