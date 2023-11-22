package com.fnaka.spproduto.infrastructure.configuration.usecases;

import com.fnaka.spproduto.application.produto.ProdutoGateway;
import com.fnaka.spproduto.application.produto.atualiza.AtualizaProdutoUseCase;
import com.fnaka.spproduto.application.produto.atualiza.DefaultAtualizaProdutoUseCase;
import com.fnaka.spproduto.application.produto.busca.BuscaProdutoPorIdUseCase;
import com.fnaka.spproduto.application.produto.busca.DefaultBuscaProdutoPorIdUseCase;
import com.fnaka.spproduto.application.produto.cria.CriaProdutoUseCase;
import com.fnaka.spproduto.application.produto.cria.DefaultCriaProdutoUseCase;
import com.fnaka.spproduto.application.produto.lista.DefaultListaProdutosUseCase;
import com.fnaka.spproduto.application.produto.lista.ListaProdutosUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class ProdutoUseCaseConfig {

    private final ProdutoGateway produtoGateway;

    public ProdutoUseCaseConfig(final ProdutoGateway produtoGateway) {
        this.produtoGateway = Objects.requireNonNull(produtoGateway);
    }

    @Bean
    public CriaProdutoUseCase criaProdutoUseCase() {
        return new DefaultCriaProdutoUseCase(produtoGateway);
    }

    @Bean
    public BuscaProdutoPorIdUseCase buscaProdutoPorIdUseCase() {
        return new DefaultBuscaProdutoPorIdUseCase(produtoGateway);
    }

    @Bean
    public AtualizaProdutoUseCase atualizaProdutoUseCase() {
        return new DefaultAtualizaProdutoUseCase(produtoGateway);
    }

    @Bean
    public ListaProdutosUseCase listaProdutosUseCase() {
        return new DefaultListaProdutosUseCase(produtoGateway);
    }
}
