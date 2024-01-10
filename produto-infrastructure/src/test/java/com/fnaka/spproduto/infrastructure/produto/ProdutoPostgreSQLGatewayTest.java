package com.fnaka.spproduto.infrastructure.produto;

import com.fnaka.spproduto.application.produto.lista.ProdutoSearchQuery;
import com.fnaka.spproduto.domain.produto.Produto;
import com.fnaka.spproduto.domain.produto.ProdutoID;
import com.fnaka.spproduto.Fixture;
import com.fnaka.spproduto.PostgreSQLGatewayTest;
import com.fnaka.spproduto.infrastructure.produto.persistence.ProdutoJpa;
import com.fnaka.spproduto.infrastructure.produto.persistence.ProdutoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@PostgreSQLGatewayTest
class ProdutoPostgreSQLGatewayTest {

    @Autowired
    private ProdutoPostgreSQLGateway produtoGateway;

    @Autowired
    private ProdutoRepository repository;

    @Test
    void givenProdutoValido_whenCallsCria_thenReturnProduto() {
        // given
        final var expectedNome = Fixture.nome();
        final var expectedPreco = Fixture.preco();
        final var expectedEstaAtivo = true;

        final var produto = Produto.newProduto(expectedNome, expectedPreco, expectedEstaAtivo);
        final var expectedId = produto.getId();

        assertEquals(0, repository.count());

        // when
        final var actualProduto = produtoGateway.cria(produto);

        // then
        assertEquals(1, repository.count());
        assertNotNull(actualProduto);
        assertEquals(expectedId, actualProduto.getId());
        assertEquals(expectedNome, actualProduto.getNome());
        assertEquals(expectedPreco, actualProduto.getPreco());
        assertEquals(expectedEstaAtivo, actualProduto.isEstaAtivo());
        assertNotNull(actualProduto.getCriadoEm());
        assertNotNull(actualProduto.getAtualizadoEm());
        assertTrue(actualProduto.getRemovidoEm().isEmpty());

        final var produtoJpa = repository.findById(expectedId.getValue()).get();
        assertEquals(expectedId.getValue(), produtoJpa.getId());
        assertEquals(expectedNome, produtoJpa.getNome());
        assertEquals(expectedPreco, produtoJpa.getPreco());
        assertEquals(expectedEstaAtivo, produtoJpa.isEstaAtivo());
        assertEquals(actualProduto.getCriadoEm(), produtoJpa.getCriadoEm());
        assertEquals(actualProduto.getAtualizadoEm(), produtoJpa.getAtualizadoEm());
        assertNull(produtoJpa.getRemovidoEm());
    }

    @Test
    void givenProdutoValido_whenCallsAtualiza_thenReturnProduto() {
        // given
        final var expectedNome = "smartphone xpto";
        final var expectedPreco = 1_000;
        final var expectedEstaAtivo = false;

        final var produto = Produto.newProduto(Fixture.nome(), Fixture.preco(), false);
        final var expectedId = produto.getId();

        assertEquals(0, repository.count());
        repository.saveAndFlush(ProdutoJpa.from(produto));
        assertEquals(1, repository.count());

        final var produtoAtualizado = produto.clone()
                .atualiza(expectedNome, expectedPreco, expectedEstaAtivo);

        // when
        final var actualProduto = produtoGateway.atualiza(produtoAtualizado);

        // then
        assertEquals(1, repository.count());
        assertNotNull(actualProduto);
        assertEquals(expectedId, actualProduto.getId());
        assertEquals(expectedNome, actualProduto.getNome());
        assertEquals(expectedPreco, actualProduto.getPreco());
        assertEquals(expectedEstaAtivo, actualProduto.isEstaAtivo());
        assertEquals(produto.getCriadoEm(), actualProduto.getCriadoEm());
        assertTrue(produto.getAtualizadoEm().isBefore(actualProduto.getAtualizadoEm()));
        assertNotNull(actualProduto.getRemovidoEm());

        final var produtoJpa = repository.findById(expectedId.getValue()).get();
        assertEquals(expectedId.getValue(), produtoJpa.getId());
        assertEquals(expectedNome, produtoJpa.getNome());
        assertEquals(expectedPreco, produtoJpa.getPreco());
        assertEquals(expectedEstaAtivo, produtoJpa.isEstaAtivo());
        assertEquals(actualProduto.getCriadoEm(), produtoJpa.getCriadoEm());
        assertEquals(actualProduto.getAtualizadoEm(), produtoJpa.getAtualizadoEm());
        assertEquals(actualProduto.getRemovidoEm().get(), produtoJpa.getRemovidoEm());
    }

    @Test
    void givenIdValido_whenCallsBuscaPorId_shouldReturnProduto() {
        // given
        final var expectedNome = "smartphone xpto";
        final var expectedPreco = 1_000;
        final var expectedEstaAtivo = false;

        final var produto = Produto.newProduto(expectedNome, expectedPreco, expectedEstaAtivo);
        final var expectedId = produto.getId();
        assertEquals(0, repository.count());
        repository.saveAndFlush(ProdutoJpa.from(produto));
        assertEquals(1, repository.count());

        // when
        final var actualProduto = produtoGateway.buscaPorId(produto.getId()).get();

        // then
        assertNotNull(actualProduto);
        assertEquals(expectedId, actualProduto.getId());
        assertEquals(expectedNome, actualProduto.getNome());
        assertEquals(expectedPreco, actualProduto.getPreco());
        assertEquals(expectedEstaAtivo, actualProduto.isEstaAtivo());
        assertEquals(produto.getCriadoEm(), actualProduto.getCriadoEm());
        assertEquals(produto.getAtualizadoEm(), actualProduto.getAtualizadoEm());
        assertEquals(produto.getRemovidoEm(), actualProduto.getRemovidoEm());
    }

    @Test
    void givenIdInvalido_whenCallsBuscaPorId_shouldReturnEmpty() {
        // given
        final var expectedNome = "smartphone xpto";
        final var expectedPreco = 1_000;
        final var expectedEstaAtivo = false;

        final var produto = Produto.newProduto(expectedNome, expectedPreco, expectedEstaAtivo);

        assertEquals(0, repository.count());
        repository.saveAndFlush(ProdutoJpa.from(produto));
        assertEquals(1, repository.count());

        // when
        final var actualProduto = produtoGateway.buscaPorId(ProdutoID.from("id-invalido"));

        // then
        assertNotNull(actualProduto);
        assertTrue(actualProduto.isEmpty());
    }

    @Test
    void givenProdutosValidos_whenCallsLista_thenReturnProdutosPaginados() {
        // given
        final var expectedPage1 = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 2;

        final var smartphone = Produto.newProduto("smartphone", Fixture.preco(), true);
        final var mouse = Produto.newProduto("mouse", Fixture.preco(), true);

        assertEquals(0, repository.count());
        repository.saveAllAndFlush(List.of(
                ProdutoJpa.from(smartphone),
                ProdutoJpa.from(mouse)
        ));
        assertEquals(2, repository.count());

        final var query1 = new ProdutoSearchQuery(
                expectedPage1,
                expectedPerPage,
                "",
                "nome",
                "asc"
        );

        // when
        final var actualResult1 = produtoGateway.lista(query1);

        // then
        assertEquals(expectedPage1, actualResult1.page());
        assertEquals(expectedPerPage, actualResult1.perPage());
        assertEquals(expectedTotal, actualResult1.total());
        assertEquals(expectedPerPage, actualResult1.items().size());
        assertEquals(mouse.getId(), actualResult1.items().get(0).getId());

        // given
        final var expectedPage2 = 1;

        final var query2 = new ProdutoSearchQuery(
                expectedPage2,
                expectedPerPage,
                "",
                "nome",
                "asc"
        );

        // when
        final var actualResult2 = produtoGateway.lista(query2);

        // then
        assertEquals(expectedPage2, actualResult2.page());
        assertEquals(expectedPerPage, actualResult2.perPage());
        assertEquals(expectedTotal, actualResult2.total());
        assertEquals(expectedPerPage, actualResult2.items().size());
        assertEquals(smartphone.getId(), actualResult2.items().get(0).getId());
    }

    @Test
    void givenProdutosValidos_whenCallsListaComTermoRod_thenReturnProdutosPaginados() {
        // given
        final var expectedPage = 0;
        final var expectedPerPage = 2;
        final var expectedTotal = 2;

        final var produtoUm = Produto.newProduto("produto Um", Fixture.preco(), true);
        final var produtoDois = Produto.newProduto("produto Dois", Fixture.preco(), true);
        final var teste = Produto.newProduto("teste", Fixture.preco(), true);

        assertEquals(0, repository.count());
        repository.saveAllAndFlush(List.of(
                ProdutoJpa.from(produtoUm),
                ProdutoJpa.from(produtoDois),
                ProdutoJpa.from(teste)
        ));
        assertEquals(3, repository.count());

        final var query = new ProdutoSearchQuery(
                expectedPage,
                expectedPerPage,
                "rod",
                "criadoEm",
                "asc"
        );

        // when
        final var actualResult = produtoGateway.lista(query);

        // then
        assertEquals(expectedPage, actualResult.page());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(expectedPerPage, actualResult.items().size());
        assertEquals(produtoUm.getId(), actualResult.items().get(0).getId());
        assertEquals(produtoDois.getId(), actualResult.items().get(1).getId());
    }
}