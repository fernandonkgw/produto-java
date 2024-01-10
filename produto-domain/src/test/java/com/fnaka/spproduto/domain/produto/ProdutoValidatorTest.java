package com.fnaka.spproduto.domain.produto;

import com.fnaka.spproduto.domain.validation.handler.Notification;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ProdutoValidatorTest {

    @Nested
    class checkNomeConstraints {

        @Test
        @DisplayName("deve adicionar erro quando nome for nulo")
        void shouldAppendErrorMessage_whenNomeNulo() {
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
        @DisplayName("deve adicionar erro quando nome for vazio")
        void shouldAppendErrorMessage_whenNomeVazio() {
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
        @DisplayName("deve adicionar erro quando nome for maior que 255")
        void shouldAppendErrorMessage_whenTamanhoNomeMaiorQue255() {
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
        @DisplayName("deve adicionar erro quando nome for menor que 3")
        void shouldAppendErrorMessage_whenTamanhoNomeMenorQue3() {
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
    }

    @Nested
    class checkPrecoConstraints {

        @Test
        @DisplayName("deve adicionar erro quando preco for nulo")
        void shouldAppendErrorMessage_whenPrecoNulo() {
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
        @DisplayName("deve adicionar erro quando preco for zero")
        void shouldAppendErrorMessage_whenPrecoZero() {
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
        @DisplayName("deve adicionar erro quando preco for menor que zero")
        void shouldAppendErrorMessage_whenPrecoMenorQueZero() {
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
    }

    @Nested
    class checkNomeAndPrecoConstraints {

        @Test
        @DisplayName("deve adicionar 2 erros quando nome for nulo e preco for zero")
        void shouldAppend2ErrorsMessage_whenNomeNuloAndPrecoZero() {
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




}