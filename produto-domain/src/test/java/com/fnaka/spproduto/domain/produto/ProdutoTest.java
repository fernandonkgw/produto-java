package com.fnaka.spproduto.domain.produto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ProdutoTest {

    @Test
    void givenParamValidos_whenCallsNewProduto_thenReturnProduto() {
        // given
        final var expectedNome = "smartphone XPTO";
        final var expectedPreco = 1_000;
        final var expectedEstaAtivo = true;

        // when
        final var actualProduto = Produto.newProduto(expectedNome, expectedPreco, expectedEstaAtivo);

        // then
        Assertions.assertNotNull(actualProduto);
        Assertions.assertNotNull(actualProduto.getId());
        Assertions.assertEquals(expectedNome, actualProduto.getNome());
        Assertions.assertEquals(expectedPreco, actualProduto.getPreco());
        Assertions.assertEquals(expectedEstaAtivo, actualProduto.isEstaAtivo());
        Assertions.assertNotNull(actualProduto.getCriadoEm());
        Assertions.assertNotNull(actualProduto.getAtualizadoEm());
        Assertions.assertEquals(actualProduto.getCriadoEm(), actualProduto.getAtualizadoEm());
        Assertions.assertTrue(actualProduto.getRemovidoEm().isEmpty());
    }

    @Test
    void givenEstaAtivoFalse_whenCallsNewProduto_thenReturnProduto() {
        // given
        final var expectedNome = "smartphone XPTO";
        final var expectedPreco = 1_000;
        final var expectedEstaAtivo = false;

        // when
        final var actualProduto = Produto.newProduto(expectedNome, expectedPreco, expectedEstaAtivo);

        // then
        Assertions.assertNotNull(actualProduto);
        Assertions.assertNotNull(actualProduto.getId());
        Assertions.assertEquals(expectedNome, actualProduto.getNome());
        Assertions.assertEquals(expectedPreco, actualProduto.getPreco());
        Assertions.assertEquals(expectedEstaAtivo, actualProduto.isEstaAtivo());
        Assertions.assertNotNull(actualProduto.getCriadoEm());
        Assertions.assertNotNull(actualProduto.getAtualizadoEm());
        Assertions.assertEquals(actualProduto.getCriadoEm(), actualProduto.getAtualizadoEm());
        Assertions.assertNotNull(actualProduto.getRemovidoEm());
        Assertions.assertEquals(actualProduto.getCriadoEm(), actualProduto.getRemovidoEm().get());
    }

    @Test
    void givenProdutoValido_whenCallsAtualizaComParamsValidos_thenReturnProduto() {
        // given
        final var produto = Produto.newProduto("smartphone", 1_000, false);
        final var expectedNome = "smatphone XPTO";
        final var expectedPreco = 2_000;
        final var expectedEstaAtivo = true;

        final var criadoEm = produto.getCriadoEm();
        final var atualizadoEm = produto.getAtualizadoEm();

        // when
        final var actualProduto = produto.atualiza(expectedNome, expectedPreco, expectedEstaAtivo);

        // then
        Assertions.assertNotNull(actualProduto);
        Assertions.assertEquals(expectedNome, actualProduto.getNome());
        Assertions.assertEquals(expectedPreco, actualProduto.getPreco());
        Assertions.assertEquals(expectedEstaAtivo, actualProduto.isEstaAtivo());
        Assertions.assertEquals(criadoEm, actualProduto.getCriadoEm());
        Assertions.assertTrue(atualizadoEm.isBefore(actualProduto.getAtualizadoEm()));
        Assertions.assertTrue(actualProduto.getRemovidoEm().isEmpty());
    }

    @Test
    void givenProdutoValido_whenCallsAtualizaComEstaAtivoFalse_thenReturnProduto() {
        // given
        final var produto = Produto.newProduto("smartphone", 1_000, true);
        final var expectedNome = "smatphone XPTO";
        final var expectedPreco = 2_000;
        final var expectedEstaAtivo = false;

        final var criadoEm = produto.getCriadoEm();
        final var atualizadoEm = produto.getAtualizadoEm();

        // when
        final var actualProduto = produto.atualiza(expectedNome, expectedPreco, expectedEstaAtivo);

        // then
        Assertions.assertNotNull(actualProduto);
        Assertions.assertEquals(expectedNome, actualProduto.getNome());
        Assertions.assertEquals(expectedPreco, actualProduto.getPreco());
        Assertions.assertEquals(expectedEstaAtivo, actualProduto.isEstaAtivo());
        Assertions.assertEquals(criadoEm, actualProduto.getCriadoEm());
        Assertions.assertTrue(atualizadoEm.isBefore(actualProduto.getAtualizadoEm()));
        Assertions.assertNotNull(actualProduto.getRemovidoEm());
        Assertions.assertEquals(actualProduto.getAtualizadoEm(), actualProduto.getRemovidoEm().get());
    }
}