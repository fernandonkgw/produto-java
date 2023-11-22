package com.fnaka.spproduto.application.produto.lista;

import com.fnaka.spproduto.application.Pagination;
import com.fnaka.spproduto.application.UseCase;

public abstract class ListaProdutosUseCase
        extends UseCase<ProdutoSearchQuery, Pagination<ListaProdutoOutput>> {
}
