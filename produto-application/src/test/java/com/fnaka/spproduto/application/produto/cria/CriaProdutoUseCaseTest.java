package com.fnaka.spproduto.application.produto.cria;

import com.fnaka.spproduto.application.Fixture;
import com.fnaka.spproduto.application.UseCaseTest;
import com.fnaka.spproduto.domain.exceptions.NotificationException;
import com.fnaka.spproduto.application.produto.ProdutoGateway;
import org.junit.jupiter.api.Assertions;
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

    @Override
    protected List<Object> getMocks() {
        return List.of(produtoGateway);
    }

    @Test
    void givenInputsValido_whenCallsExecute_thenReturnOutput() {
        // given
        final var expectedNome = Fixture.nome();
        final var expectedPreco = Fixture.preco();
        final var expectedEstaAtivo = true;

        final var input = CriaProdutoInput.with(expectedNome, expectedPreco, expectedEstaAtivo);

        Mockito.when(produtoGateway.cria(ArgumentMatchers.any()))
                .thenAnswer(AdditionalAnswers.returnsFirstArg());

        // when
        final var actualOutput = useCase.execute(input);

        // then
        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        Mockito.verify(produtoGateway).cria(ArgumentMatchers.argThat(aProduto ->
                Objects.nonNull(aProduto.getId()) &&
                        Objects.equals(expectedNome, aProduto.getNome()) &&
                        Objects.equals(expectedPreco, aProduto.getPreco()) &&
                        Objects.equals(expectedEstaAtivo, aProduto.isEstaAtivo()) &&
                        Objects.nonNull(aProduto.getCriadoEm()) &&
                        Objects.nonNull(aProduto.getAtualizadoEm())
        ));
    }

    @Test
    void givenNomeNulo_whenCallsExecute_thenThrowsNotificationException() {
        // given
        final var expectedPreco = Fixture.preco();
        final var expectedEstaAtivo = true;

        final var input = CriaProdutoInput.with(null, expectedPreco, expectedEstaAtivo);

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
    void givenNomeNuloAndPrecoZero_whenCallsExecute_thenThrowsNotificationException() {
        // given
        final var expectedEstaAtivo = true;

        final var input = CriaProdutoInput.with(null, 0, expectedEstaAtivo);

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
    void givenInputsValidos_whenCallsExecuteAndProdutoGatewayThrowsException_thenThrowsNotificationException() {
        // given
        final var expectedNome = Fixture.nome();
        final var expectedPreco = Fixture.preco();
        final var expectedEstaAtivo = true;

        final var input = CriaProdutoInput.with(expectedNome, expectedPreco, expectedEstaAtivo);

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