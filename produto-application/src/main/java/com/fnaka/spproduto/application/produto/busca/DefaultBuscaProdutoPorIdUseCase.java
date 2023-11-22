package com.fnaka.spproduto.application.produto.busca;

import com.fnaka.spproduto.domain.exceptions.NotFoundException;
import com.fnaka.spproduto.domain.produto.Produto;
import com.fnaka.spproduto.application.produto.ProdutoGateway;
import com.fnaka.spproduto.domain.produto.ProdutoID;

import java.util.Objects;

public class DefaultBuscaProdutoPorIdUseCase extends BuscaProdutoPorIdUseCase {

    private final ProdutoGateway produtoGateway;

    public DefaultBuscaProdutoPorIdUseCase(final ProdutoGateway produtoGateway) {
        this.produtoGateway = Objects.requireNonNull(produtoGateway);
    }

    @Override
    public ProdutoOutput execute(final String id) {
        final var produtoId = ProdutoID.from(id);
        return this.produtoGateway.buscaPorId(produtoId)
                .map(ProdutoOutput::from)
                .orElseThrow(() -> NotFoundException.with(Produto.class, produtoId));
    }
}
