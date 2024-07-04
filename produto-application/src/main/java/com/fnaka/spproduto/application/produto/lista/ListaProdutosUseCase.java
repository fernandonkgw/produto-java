package com.fnaka.spproduto.application.produto.lista;

import com.fnaka.spproduto.application.Pagination;
import com.fnaka.spproduto.application.UseCase;

public interface ListaProdutosUseCase
        extends UseCase<ProdutoSearchQuery, Pagination<ListaProdutoOutput>> {
}
