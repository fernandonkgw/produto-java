package com.fnaka.spproduto.application.produto.atualiza;

import com.fnaka.spproduto.application.Fixture;
import com.fnaka.spproduto.application.UseCaseTest;
import com.fnaka.spproduto.domain.exceptions.NotFoundException;
import com.fnaka.spproduto.domain.exceptions.NotificationException;
import com.fnaka.spproduto.domain.produto.Produto;
import com.fnaka.spproduto.application.produto.ProdutoGateway;
import com.fnaka.spproduto.domain.produto.ProdutoID;
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
                .thenReturn(Optional.of(produto.copy()));

        when(produtoGateway.atualiza(any()))
                .thenAnswer(returnsFirstArg());

        // when
        final var actualOutput = useCase.execute(input);

        // then
        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        verify(produtoGateway).buscaPorId(eq(expectedId));

        verify(produtoGateway).atualiza(argThat(produtoAtualizado ->
                Objects.equals(expectedId, produtoAtualizado.getId())
                        && Objects.equals(expectedNome, produtoAtualizado.getNome())
                        && Objects.equals(expectedPreco, produtoAtualizado.getPreco())
                        && Objects.equals(expectedEstaAtivo, produtoAtualizado.isEstaAtivo())
                        && Objects.equals(produto.getCriadoEm(), produtoAtualizado.getCriadoEm())
                        && produto.getAtualizadoEm().isBefore(produtoAtualizado.getAtualizadoEm())
                        && Objects.isNull(produtoAtualizado.getRemovidoEm())
                ));

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
                .thenReturn(Optional.of(produto.copy()));


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