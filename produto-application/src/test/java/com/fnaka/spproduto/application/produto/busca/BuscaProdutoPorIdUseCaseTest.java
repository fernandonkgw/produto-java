package com.fnaka.spproduto.application.produto.busca;

import com.fnaka.spproduto.application.Fixture;
import com.fnaka.spproduto.application.UseCaseTest;
import com.fnaka.spproduto.domain.exceptions.NotFoundException;
import com.fnaka.spproduto.domain.produto.Produto;
import com.fnaka.spproduto.application.produto.ProdutoGateway;
import com.fnaka.spproduto.domain.produto.ProdutoID;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BuscaProdutoPorIdUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultBuscaProdutoPorIdUseCase useCase;

    @Mock
    private ProdutoGateway produtoGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(produtoGateway);
    }

    @Test
    void givenIdValido_whenCallsExecute_thenReturnOutput() {
        // given
        final var expectedNome = Fixture.nome();
        final var expectedPreco = Fixture.preco();
        final var expectedEstaAtivo = true;

        final var produto = Produto.newProduto(expectedNome, expectedPreco, expectedEstaAtivo);
        final var expectedId = produto.getId();

        when(produtoGateway.buscaPorId(expectedId))
                .thenReturn(Optional.of(produto));

        // when
        final var actualOutput = useCase.execute(expectedId.getValue());

        // then
        assertNotNull(actualOutput);
        assertEquals(expectedId.getValue(), actualOutput.id());
        assertEquals(expectedNome, actualOutput.nome());
        assertEquals(expectedPreco, actualOutput.preco());
        assertEquals(expectedEstaAtivo, actualOutput.estaAtivo());
        assertEquals(produto.getCriadoEm(), actualOutput.criadoEm());
        assertEquals(produto.getAtualizadoEm(), actualOutput.atualizadoEm());

        verify(produtoGateway).buscaPorId(eq(expectedId));
    }

    @Test
    void givenIdInvalido_whenCallsExecute_thenThrowsNotFoundException() {
        // given
        final var expectedId = ProdutoID.from("id-invalido");
        final var expectedErrorMessage = "Produto com ID id-invalido nao encontrado";

        when(produtoGateway.buscaPorId(expectedId))
                .thenReturn(Optional.empty());

        // when
        final var actualException = assertThrows(
                NotFoundException.class, () -> useCase.execute(expectedId.getValue())
        );

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(produtoGateway).buscaPorId(eq(expectedId));
    }
}