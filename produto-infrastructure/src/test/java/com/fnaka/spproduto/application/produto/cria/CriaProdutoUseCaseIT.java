package com.fnaka.spproduto.application.produto.cria;

import com.fnaka.spproduto.application.produto.ProdutoGateway;
import com.fnaka.spproduto.domain.exceptions.NotificationException;
import com.fnaka.spproduto.Fixture;
import com.fnaka.spproduto.IntegrationTest;
import com.fnaka.spproduto.infrastructure.produto.persistence.ProdutoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@IntegrationTest
class CriaProdutoUseCaseIT {

    @Autowired
    private CriaProdutoUseCase useCase;

    @Autowired
    private ProdutoRepository produtoRepository;

    @SpyBean
    private ProdutoGateway produtoGateway;

    @Test
    void givenInputValido_whenCallsExecute_thenReturnOutput() {
        // given
        final var expectedNome = Fixture.nome();
        final var expectedPreco = Fixture.preco();
        final var expectedEstaAtivo = true;

        final var input = CriaProdutoInput.with(
                expectedNome,
                expectedPreco,
                expectedEstaAtivo
        );

        // when
        final var actualOutput = useCase.execute(input);

        // then
        assertEquals(1, produtoRepository.count());
        assertNotNull(actualOutput.id());

        final var produtoJpa = produtoRepository.findById(actualOutput.id()).get();
        assertEquals(expectedNome, produtoJpa.getNome());
        assertEquals(expectedPreco, produtoJpa.getPreco());
        assertEquals(expectedEstaAtivo, produtoJpa.isEstaAtivo());
        assertNotNull(produtoJpa.getCriadoEm());
        assertNotNull(produtoJpa.getAtualizadoEm());
        assertNull(produtoJpa.getRemovidoEm());

        verify(produtoGateway).cria(any());
    }

    @Test
    void givenNomeNulo_whenCallsExecute_thenThrowsNotificationException() {
        // given
        final var expectedPreco = Fixture.preco();
        final var expectedEstaAtivo = true;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'nome' nao deve ser nulo";

        final var input = CriaProdutoInput.with(
                null,
                expectedPreco,
                expectedEstaAtivo
        );

        // when
        final var actualException = assertThrows(
                NotificationException.class, () -> useCase.execute(input)
        );

        // then
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(produtoGateway, never()).cria(any());
    }
}
