package com.fnaka.spproduto.infrastructure.api.controllers;

import com.fnaka.spproduto.application.produto.busca.BuscaProdutoPorIdUseCase;
import com.fnaka.spproduto.application.produto.cria.CriaProdutoInput;
import com.fnaka.spproduto.application.produto.cria.CriaProdutoUseCase;
import com.fnaka.spproduto.infrastructure.api.ProdutoAPI;
import com.fnaka.spproduto.infrastructure.produto.models.CriaProdutoRequest;
import com.fnaka.spproduto.infrastructure.produto.models.ProdutoResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;

@RestController
public class ProdutoController implements ProdutoAPI {

    private final CriaProdutoUseCase criaProdutoUseCase;
    private final BuscaProdutoPorIdUseCase buscaProdutoPorIdUseCase;

    public ProdutoController(
            final CriaProdutoUseCase criaProdutoUseCase,
            final BuscaProdutoPorIdUseCase buscaProdutoPorIdUseCase
    ) {
        this.criaProdutoUseCase = Objects.requireNonNull(criaProdutoUseCase);
        this.buscaProdutoPorIdUseCase = Objects.requireNonNull(buscaProdutoPorIdUseCase);
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
}
