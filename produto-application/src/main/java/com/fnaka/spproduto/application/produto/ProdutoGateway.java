package com.fnaka.spproduto.application.produto;

import com.fnaka.spproduto.application.Pagination;
import com.fnaka.spproduto.application.produto.lista.ProdutoSearchQuery;
import com.fnaka.spproduto.domain.produto.Produto;
import com.fnaka.spproduto.domain.produto.ProdutoID;

import java.util.Optional;

public interface ProdutoGateway {

    Produto cria(Produto produto);

    Optional<Produto> buscaPorId(ProdutoID id);

    Produto atualiza(Produto produto);

    Pagination<Produto> lista(ProdutoSearchQuery query);
}
