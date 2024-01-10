package com.fnaka.spproduto.application.produto.atualiza;

import com.fnaka.spproduto.application.Fixture;
import com.fnaka.spproduto.application.UseCaseTest;
import com.fnaka.spproduto.domain.exceptions.NotFoundException;
import com.fnaka.spproduto.domain.exceptions.NotificationException;
import com.fnaka.spproduto.domain.produto.Produto;
import com.fnaka.spproduto.application.produto.ProdutoGateway;
import com.fnaka.spproduto.domain.produto.ProdutoID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class AtualizaProdutoUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultAtualizaProdutoUseCase useCase;

    @Mock
    private ProdutoGateway produtoGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(produtoGateway);
    }

    @Test
    void givenInputsValidos_whenCallsExecute_thenReturnOutput() {
        // given
        final var produto = Produto.newProduto(Fixture.nome(), Fixture.preco(), true);
        final var expectedId = produto.getId();
        final var expectedNome = "smartphone XPTO";
        final var expectedPreco = 1_000;
        final var expectedEstaAtivo = true;

        final var input = AtualizaProdutoInput.with(
                expectedId.getValue(),
                expectedNome,
                expectedPreco,
                expectedEstaAtivo
        );

        when(produtoGateway.buscaPorId(any()))
                .thenReturn(Optional.of(produto.clone()));

        when(produtoGateway.atualiza(any()))
                .thenAnswer(returnsFirstArg());

        // when
        final var actualOutput = useCase.execute(input);

        // then
        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        verify(produtoGateway).buscaPorId(eq(expectedId));

        final var produtoCaptor = ArgumentCaptor.forClass(Produto.class);
        verify(produtoGateway).atualiza(produtoCaptor.capture());
        final var produtoCaptured = produtoCaptor.getValue();
        Assertions.assertEquals(expectedId, produtoCaptured.getId());
        Assertions.assertEquals(expectedNome, produtoCaptured.getNome());
        Assertions.assertEquals(expectedPreco, produtoCaptured.getPreco());
        Assertions.assertEquals(expectedEstaAtivo, produtoCaptured.isEstaAtivo());
        Assertions.assertEquals(produto.getCriadoEm(), produtoCaptured.getCriadoEm());
        Assertions.assertTrue(produto.getAtualizadoEm().isBefore(produtoCaptured.getAtualizadoEm()));
        Assertions.assertTrue(produtoCaptured.getRemovidoEm().isEmpty());
    }

    @Test
    void givenNomeNulo_whenCallsExecute_thenThrowsNotificationException() {
        // given
        final var produto = Produto.newProduto(Fixture.nome(), Fixture.preco(), true);
        final var expectedId = produto.getId();
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'nome' nao deve ser nulo";

        final var input = AtualizaProdutoInput.with(
                expectedId.getValue(),
                null,
                1_000,
                true
        );

        when(produtoGateway.buscaPorId(any()))
                .thenReturn(Optional.of(produto.clone()));


        // when
        final var actualException = assertThrows(
                NotificationException.class, () -> useCase.execute(input)
        );

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(produtoGateway).buscaPorId(eq(expectedId));
        verify(produtoGateway, never()).atualiza(any());
    }

    @Test
    void givenIdInvalido_whenCallsExecute_thenThrowsNotFoundException() {
        // given
        final var expectedId = ProdutoID.from("id-invalido");
        final var expectedErrorMessage = "Produto com ID id-invalido nao encontrado";

        final var input = AtualizaProdutoInput.with(
                expectedId.getValue(),
                Fixture.nome(),
                Fixture.preco(),
                true
        );

        when(produtoGateway.buscaPorId(any()))
                .thenReturn(Optional.empty());

        // when
        final var actualException = assertThrows(
                NotFoundException.class, () -> useCase.execute(input)
        );

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(produtoGateway).buscaPorId(eq(expectedId));
        verify(produtoGateway, never()).atualiza(any());
    }
}