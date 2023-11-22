package com.fnaka.spproduto.application.produto.lista;

import com.fnaka.spproduto.application.Fixture;
import com.fnaka.spproduto.application.Pagination;
import com.fnaka.spproduto.application.UseCaseTest;
import com.fnaka.spproduto.application.produto.ProdutoGateway;
import com.fnaka.spproduto.domain.produto.Produto;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ListaProdutosUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultListaProdutosUseCase useCase;

    @Mock
    private ProdutoGateway produtoGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(produtoGateway);
    }

    @Test
    void givenQueryValida_whenCallsExecute_thenReturnPaginationOutput() {
        // given
        final var produtos = List.of(
                Produto.newProduto(Fixture.nome(), Fixture.preco(), true),
                Produto.newProduto(Fixture.nome(), Fixture.preco(), true)
        );

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTermo = "Algo";
        final var expectedSort = "criadoEm";
        final var expectedDirection = "asc";
        final var expectedTotal = 2;
        final var expectedItems = produtos.stream()
                .map(ListaProdutoOutput::from)
                .toList();
        final var expectedPagination = new Pagination<>(
                expectedPage,
                expectedPerPage,
                expectedTotal,
                produtos
        );

        when(produtoGateway.lista(any()))
                .thenReturn(expectedPagination);

        final var query =
                new ProdutoSearchQuery(expectedPage, expectedPerPage, expectedTermo, expectedSort, expectedDirection);

        // when
        final var actualOutput = useCase.execute(query);

        // then
        assertEquals(expectedPage, actualOutput.page());
        assertEquals(expectedPerPage, actualOutput.perPage());
        assertEquals(expectedTotal, actualOutput.total());
        assertEquals(expectedItems, actualOutput.items());

        verify(produtoGateway).lista(argThat(searchQuery ->
                Objects.equals(expectedPage, searchQuery.page())
                        && Objects.equals(expectedPerPage, searchQuery.perPage())
                        && Objects.equals(expectedTermo, searchQuery.termo())
                        && Objects.equals(expectedSort, searchQuery.sort())
                        && Objects.equals(expectedDirection, searchQuery.direction())
        ));
    }

    @Test
    void givenQueryValidaComRetornoVazio_whenCallsExecute_thenReturnPaginationOutput() {
        // given
        final var produtos = List.<Produto>of();

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTermo = "Algo";
        final var expectedSort = "criadoEm";
        final var expectedDirection = "asc";
        final var expectedTotal = 0;
        final var expectedItems = List.<ListaProdutoOutput>of();
        final var expectedPagination = new Pagination<>(
                expectedPage,
                expectedPerPage,
                expectedTotal,
                produtos
        );

        when(produtoGateway.lista(any()))
                .thenReturn(expectedPagination);

        final var query =
                new ProdutoSearchQuery(expectedPage, expectedPerPage, expectedTermo, expectedSort, expectedDirection);

        // when
        final var actualOutput = useCase.execute(query);

        // then
        assertEquals(expectedPage, actualOutput.page());
        assertEquals(expectedPerPage, actualOutput.perPage());
        assertEquals(expectedTotal, actualOutput.total());
        assertEquals(expectedItems, actualOutput.items());

        verify(produtoGateway).lista(argThat(searchQuery ->
                Objects.equals(expectedPage, searchQuery.page())
                        && Objects.equals(expectedPerPage, searchQuery.perPage())
                        && Objects.equals(expectedTermo, searchQuery.termo())
                        && Objects.equals(expectedSort, searchQuery.sort())
                        && Objects.equals(expectedDirection, searchQuery.direction())
        ));
    }


}