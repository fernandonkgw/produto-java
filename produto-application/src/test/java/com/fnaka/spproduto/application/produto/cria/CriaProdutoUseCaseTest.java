package com.fnaka.spproduto.application.produto.cria;

import com.fnaka.spproduto.application.Fixture;
import com.fnaka.spproduto.application.UseCaseTest;
import com.fnaka.spproduto.domain.exceptions.NotificationException;
import com.fnaka.spproduto.application.produto.ProdutoGateway;
import com.fnaka.spproduto.domain.produto.Produto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CriaProdutoUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultCriaProdutoUseCase useCase;

    @Mock
    private ProdutoGateway produtoGateway;

    @Captor
    private ArgumentCaptor<Produto> produtoArgumentCaptor;

    @Override
    protected List<Object> getMocks() {
        return List.of(produtoGateway);
    }

    @Nested
    class execute {

        @Test
        @DisplayName("deve retornar Output do produto criado")
        void shouldReturnOutput() {
            // given
            final var expectedNome = Fixture.nome();
            final var expectedPreco = Fixture.preco();
            final var expectedEstaAtivo = true;

            final var input = CriaProdutoUseCase.Input.with(expectedNome, expectedPreco, expectedEstaAtivo);

            Mockito.when(produtoGateway.cria(produtoArgumentCaptor.capture()))
                    .thenAnswer(AdditionalAnswers.returnsFirstArg());

            // when
            final var actualOutput = useCase.execute(input);

            // then
            assertNotNull(actualOutput);
            assertNotNull(actualOutput.id());

            final var produtoCaptured = produtoArgumentCaptor.getValue();
            Assertions.assertNotNull(produtoCaptured.getId());
            Assertions.assertEquals(expectedNome, produtoCaptured.getNome());
            Assertions.assertEquals(expectedPreco, produtoCaptured.getPreco());
            Assertions.assertEquals(expectedEstaAtivo, produtoCaptured.isEstaAtivo());
            Assertions.assertNotNull(produtoCaptured.getCriadoEm());
            Assertions.assertNotNull(produtoCaptured.getAtualizadoEm());
        }

        @Test
        @DisplayName("deve lancar NotificationException quando nome nulo")
        void shouldThrowsNotificationException_whenNullNome() {
            // given
            final var expectedPreco = Fixture.preco();
            final var expectedEstaAtivo = true;

            final var input = CriaProdutoUseCase.Input.with(null, expectedPreco, expectedEstaAtivo);

            final var expectedErrorCount = 1;
            final var expectedErrorMessage = "'nome' nao deve ser nulo";

            // when
            final var actualException = Assertions.assertThrows(
                    NotificationException.class, () -> useCase.execute(input)
            );

            // then
            assertNotNull(actualException);
            assertEquals(expectedErrorCount, actualException.getErrors().size());
            assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

            Mockito.verify(produtoGateway, Mockito.never()).cria(ArgumentMatchers.any());
        }

        @Test
        @DisplayName("deve lancar NotificationException quando nome nulo e preco igual a zero")
        void shouldThrowsNotificationException_whenNullNomeAndPrecoEqualsToZero() {
            // given
            final var expectedEstaAtivo = true;

            final var input = CriaProdutoUseCase.Input.with(null, 0, expectedEstaAtivo);

            final var expectedErrorCount = 2;
            final var expectedErrorMessage1 = "'nome' nao deve ser nulo";
            final var expectedErrorMessage2 = "'preco' deve ser maior que zero. Preco informado: 0";

            // when
            final var actualException = Assertions.assertThrows(
                    NotificationException.class, () -> useCase.execute(input)
            );

            // then
            assertNotNull(actualException);
            assertEquals(expectedErrorCount, actualException.getErrors().size());
            assertEquals(expectedErrorMessage1, actualException.getErrors().get(0).message());
            assertEquals(expectedErrorMessage2, actualException.getErrors().get(1).message());

            Mockito.verify(produtoGateway, Mockito.never()).cria(ArgumentMatchers.any());
        }

        @Test
        @DisplayName("deve lancar NotificationException quando ProdutoGateway lancar Exception")
        void shouldThrowsNotificationException_whenProdutoGatewayThrowsException() {
            // given
            final var expectedNome = Fixture.nome();
            final var expectedPreco = Fixture.preco();
            final var expectedEstaAtivo = true;

            final var input = CriaProdutoUseCase.Input.with(expectedNome, expectedPreco, expectedEstaAtivo);

            Mockito.doThrow(new IllegalStateException("Gateway error"))
                    .when(produtoGateway).cria(ArgumentMatchers.any());

            // when
            Assertions.assertThrows(
                    IllegalStateException.class, () -> useCase.execute(input)
            );

            Mockito.verify(produtoGateway).cria(ArgumentMatchers.argThat(aProduto ->
                    Objects.nonNull(aProduto.getId()) &&
                            Objects.equals(expectedNome, aProduto.getNome()) &&
                            Objects.equals(expectedPreco, aProduto.getPreco()) &&
                            Objects.equals(expectedEstaAtivo, aProduto.isEstaAtivo()) &&
                            Objects.nonNull(aProduto.getCriadoEm()) &&
                            Objects.nonNull(aProduto.getAtualizadoEm())
            ));
        }
    }

}