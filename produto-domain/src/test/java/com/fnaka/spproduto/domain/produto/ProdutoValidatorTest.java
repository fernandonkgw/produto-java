package com.fnaka.spproduto.domain.produto;

import com.fnaka.spproduto.domain.validation.handler.Notification;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ProdutoValidatorTest {

    @Test
    void givenNomeNulo_whenCallsValidate_thenAppendErrorMessage() {
        // given
        final var expectedProduto = Produto.newProduto(null, 1000, true);
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'nome' nao deve ser nulo";

        final var notification = Notification.create();
        Assertions.assertFalse(notification.hasErrors());

        // when
        expectedProduto.validate(notification);

        // then
        Assertions.assertTrue(notification.hasErrors());
        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());
    }

    @Test
    void givenNomeVazio_whenCallsValidate_thenAppendErrorMessage() {
        // given
        final var expectedProduto = Produto.newProduto("", 1000, true);
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'nome' nao deve ser vazio";

        final var notification = Notification.create();
        Assertions.assertFalse(notification.hasErrors());

        // when
        expectedProduto.validate(notification);

        // then
        Assertions.assertTrue(notification.hasErrors());
        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());
    }

    @Test
    void givenTamanhoNomeMaiorQue255_whenCallsValidate_thenAppendErrorMessage() {
        // given
        final var expectedNome = "a".repeat(256);
        final var expectedProduto = Produto.newProduto(expectedNome, 1000, true);
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'nome' deve ser entre 3 a 255 characters. Nome informado: %s"
                .formatted(expectedNome);

        final var notification = Notification.create();
        Assertions.assertFalse(notification.hasErrors());

        // when
        expectedProduto.validate(notification);

        // then
        Assertions.assertTrue(notification.hasErrors());
        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());
    }

    @Test
    void givenTamanhoNomeMenorQue3_whenCallsValidate_thenAppendErrorMessage() {
        // given
        final var expectedNome = "aa";
        final var expectedProduto = Produto.newProduto(expectedNome, 1000, true);
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'nome' deve ser entre 3 a 255 characters. Nome informado: %s"
                .formatted(expectedNome);

        final var notification = Notification.create();
        Assertions.assertFalse(notification.hasErrors());

        // when
        expectedProduto.validate(notification);

        // then
        Assertions.assertTrue(notification.hasErrors());
        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());
    }

    @Test
    void givenPrecoNulo_whenCallsValidate_thenAppendErrorMessage() {
        // given
        final var expectedProduto = Produto.newProduto("smartphone XPTO", null, true);
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'preco' nao deve ser nulo";

        final var notification = Notification.create();
        Assertions.assertFalse(notification.hasErrors());

        // when
        expectedProduto.validate(notification);

        // then
        Assertions.assertTrue(notification.hasErrors());
        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());
    }

    @Test
    void givenPrecoZero_whenCallsValidate_thenAppendErrorMessage() {
        // given
        final var expectedPreco = 0;
        final var expectedProduto = Produto.newProduto("smartphone XPTO", expectedPreco, true);
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'preco' deve ser maior que zero. Preco informado: %s"
                .formatted(expectedPreco);

        final var notification = Notification.create();
        Assertions.assertFalse(notification.hasErrors());

        // when
        expectedProduto.validate(notification);

        // then
        Assertions.assertTrue(notification.hasErrors());
        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());
    }

    @Test
    void givenPrecoMenorQueZero_whenCallsValidate_thenAppendErrorMessage() {
        // given
        final var expectedPreco = -10;
        final var expectedProduto = Produto.newProduto("smartphone XPTO", expectedPreco, true);
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'preco' deve ser maior que zero. Preco informado: %s"
                .formatted(expectedPreco);

        final var notification = Notification.create();
        Assertions.assertFalse(notification.hasErrors());

        // when
        expectedProduto.validate(notification);

        // then
        Assertions.assertTrue(notification.hasErrors());
        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());
    }

    @Test
    void givenNomeNuloAndPrecoZero_whenCallsValidate_thenAppend2ErrorMessage() {
        // given
        final var expectedPreco = -10;
        final var expectedProduto = Produto.newProduto(null, expectedPreco, true);
        final var expectedErrorCount = 2;
        final var expectedErrorMessage1 = "'nome' nao deve ser nulo";
        final var expectedErrorMessage2 = "'preco' deve ser maior que zero. Preco informado: %s"
                .formatted(expectedPreco);

        final var notification = Notification.create();
        Assertions.assertFalse(notification.hasErrors());

        // when
        expectedProduto.validate(notification);

        // then
        Assertions.assertTrue(notification.hasErrors());
        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage1, notification.firstError().message());
        Assertions.assertEquals(expectedErrorMessage2, notification.getErrors().get(1).message());
    }
}