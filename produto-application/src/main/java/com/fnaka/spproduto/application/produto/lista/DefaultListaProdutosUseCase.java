package com.fnaka.spproduto.application.produto.lista;

import com.fnaka.spproduto.application.Pagination;
import com.fnaka.spproduto.application.produto.ProdutoGateway;

import java.util.Objects;

public class DefaultListaProdutosUseCase implements ListaProdutosUseCase {

    private final ProdutoGateway produtoGateway;

    public DefaultListaProdutosUseCase(final ProdutoGateway produtoGateway) {
        this.produtoGateway = Objects.requireNonNull(produtoGateway);
    }

    @Override
    public Pagination<ListaProdutoOutput> execute(final ProdutoSearchQuery query) {
        return this.produtoGateway.lista(query)
                .map(ListaProdutoOutput::from);
    }
}
