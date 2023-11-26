package com.fnaka.spproduto.infrastructure.api.controllers;

import com.fnaka.spproduto.application.Pagination;
import com.fnaka.spproduto.application.produto.atualiza.AtualizaProdutoInput;
import com.fnaka.spproduto.application.produto.atualiza.AtualizaProdutoUseCase;
import com.fnaka.spproduto.application.produto.busca.BuscaProdutoPorIdUseCase;
import com.fnaka.spproduto.application.produto.cria.CriaProdutoInput;
import com.fnaka.spproduto.application.produto.cria.CriaProdutoUseCase;
import com.fnaka.spproduto.application.produto.lista.ListaProdutosUseCase;
import com.fnaka.spproduto.application.produto.lista.ProdutoSearchQuery;
import com.fnaka.spproduto.infrastructure.api.ProdutoAPI;
import com.fnaka.spproduto.infrastructure.produto.models.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;

@RestController
public class ProdutoController implements ProdutoAPI {

    private final CriaProdutoUseCase criaProdutoUseCase;
    private final BuscaProdutoPorIdUseCase buscaProdutoPorIdUseCase;
    private final AtualizaProdutoUseCase atualizaProdutoUseCase;
    private final ListaProdutosUseCase listaProdutosUseCase;

    public ProdutoController(
            final CriaProdutoUseCase criaProdutoUseCase,
            final BuscaProdutoPorIdUseCase buscaProdutoPorIdUseCase,
            final AtualizaProdutoUseCase atualizaProdutoUseCase,
            final ListaProdutosUseCase listaProdutosUseCase
    ) {
        this.criaProdutoUseCase = Objects.requireNonNull(criaProdutoUseCase);
        this.buscaProdutoPorIdUseCase = Objects.requireNonNull(buscaProdutoPorIdUseCase);
        this.atualizaProdutoUseCase = Objects.requireNonNull(atualizaProdutoUseCase);
        this.listaProdutosUseCase = Objects.requireNonNull(listaProdutosUseCase);
    }

    @Override
    public ResponseEntity<?> cria(final CriaProdutoRequest body) {
        final var input = CriaProdutoInput.with(
                body.nome(),
                body.preco(),
                body.estaAtivo()
        );

        final var output = this.criaProdutoUseCase.execute(input);

        return ResponseEntity.created(URI.create("/produtos/" + output.id()))
                .body(output);
    }

    @Override
    public ProdutoResponse buscaPorId(final String id) {
        return ProdutoResponse.from(this.buscaProdutoPorIdUseCase.execute(id));
    }

    @Override
    public ResponseEntity<?> atualizaPorId(
            final String id, final AtualizaProdutoRequest body
    ) {
        final var input = AtualizaProdutoInput.with(
                id,
                body.nome(),
                body.preco(),
                body.estaAtivo()
        );

        final var output = this.atualizaProdutoUseCase.execute(input);

        return ResponseEntity.ok(AtualizaProdutoResponse.from(output));
    }

    @Override
    public Pagination<ListaProdutoResponse> lista(
            final int page, 
            final int perPage,
            final String termo,
            final String sort,
            final String direction
    ) {
        final var query = new ProdutoSearchQuery(page, perPage, termo, sort, direction);
        return this.listaProdutosUseCase.execute(query)
                .map(ListaProdutoResponse::from);
    }
}
