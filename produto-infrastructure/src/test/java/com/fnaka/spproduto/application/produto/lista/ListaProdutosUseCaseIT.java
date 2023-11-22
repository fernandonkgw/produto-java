package com.fnaka.spproduto.application.produto.lista;

import com.fnaka.spproduto.application.produto.ProdutoGateway;
import com.fnaka.spproduto.domain.produto.Produto;
import com.fnaka.spproduto.Fixture;
import com.fnaka.spproduto.IntegrationTest;
import com.fnaka.spproduto.infrastructure.produto.persistence.ProdutoJpa;
import com.fnaka.spproduto.infrastructure.produto.persistence.ProdutoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@IntegrationTest
class ListaProdutosUseCaseIT {

    @Autowired
    private ListaProdutosUseCase useCase;

    @Autowired
    private ProdutoRepository produtoRepository;

    @SpyBean
    private ProdutoGateway produtoGateway;

    @Test
    void givenQueryValida_whenCallsExecute_thenReturnPaginationOutput() {
        // given
        final var produtos = List.of(
                Produto.newProduto("AlgoUm", Fixture.preco(), true),
                Produto.newProduto("AlgoDois", Fixture.preco(), true)
        );
        this.produtoRepository.saveAllAndFlush(
                produtos.stream()
                        .map(ProdutoJpa::from)
                        .toList()
        );

        assertEquals(2, produtoRepository.count());

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTermo = "Algo";
        final var expectedSort = "criadoEm";
        final var expectedDirection = "asc";
        final var expectedTotal = 2;
        final var expectedItems = produtos.stream()
                .map(ListaProdutoOutput::from)
                .toList();

        final var query = new ProdutoSearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTermo,
                expectedSort,
                expectedDirection
        );

        // when
        final var actualOutput = useCase.execute(query);

        // then
        assertEquals(expectedPage, actualOutput.page());
        assertEquals(expectedPerPage, actualOutput.perPage());
        assertEquals(expectedTotal, actualOutput.total());
        assertTrue(expectedItems.size() == actualOutput.items().size()
                && expectedItems.containsAll(actualOutput.items())
        );

        verify(produtoGateway).lista(any());
    }

    @Test
    void givenQueryValidaComRetornoVazio_whenCallsExecute_thenReturnPaginationOutput() {
        // given
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTermo = "Algo";
        final var expectedSort = "criadoEm";
        final var expectedDirection = "asc";
        final var expectedTotal = 0;

        final var query = new ProdutoSearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTermo,
                expectedSort,
                expectedDirection
        );

        // when
        final var actualOutput = useCase.execute(query);

        // then
        assertEquals(expectedPage, actualOutput.page());
        assertEquals(expectedPerPage, actualOutput.perPage());
        assertEquals(expectedTotal, actualOutput.total());
        assertTrue(actualOutput.items().isEmpty());

        verify(produtoGateway).lista(any());
    }
}
