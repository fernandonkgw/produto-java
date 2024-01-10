package com.fnaka.spproduto.application.produto.atualiza;

import com.fnaka.spproduto.application.produto.ProdutoGateway;
import com.fnaka.spproduto.domain.exceptions.NotFoundException;
import com.fnaka.spproduto.domain.exceptions.NotificationException;
import com.fnaka.spproduto.domain.produto.Produto;
import com.fnaka.spproduto.domain.produto.ProdutoID;
import com.fnaka.spproduto.Fixture;
import com.fnaka.spproduto.IntegrationTest;
import com.fnaka.spproduto.infrastructure.produto.persistence.ProdutoJpa;
import com.fnaka.spproduto.infrastructure.produto.persistence.ProdutoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@IntegrationTest
class AtualizaProdutoUseCaseIT {

    @Autowired
    private AtualizaProdutoUseCase useCase;

    @Autowired
    private ProdutoRepository produtoRepository;

    @SpyBean
    private ProdutoGateway produtoGateway;

    @Test
    void givenInputsValidos_whenCallsExecute_thenReturnOutput() {
        // given
        final var produto = Produto.newProduto(Fixture.nome(), Fixture.preco(), true);

        this.produtoRepository.saveAndFlush(ProdutoJpa.from(produto));
        assertEquals(1, this.produtoRepository.count());

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

        // when
        final var actualOutput = useCase.execute(input);

        // then
        assertNotNull(actualOutput);
        assertEquals(expectedId.getValue(), actualOutput.id());

        final var produtoJpa = this.produtoRepository.findById(actualOutput.id()).get();

        assertEquals(expectedNome, produtoJpa.getNome());
        assertEquals(expectedPreco, produtoJpa.getPreco());
        assertEquals(expectedEstaAtivo, produtoJpa.isEstaAtivo());
        assertTrue(produto.getAtualizadoEm().isBefore(produtoJpa.getAtualizadoEm()));

        verify(produtoGateway).buscaPorId(any());

        verify(produtoGateway).atualiza(any());

    }

    @Test
    void givenNomeNulo_whenCallsExecute_thenThrowsNotificationException() {
        // given
        final var produto = Produto.newProduto(Fixture.nome(), Fixture.preco(), true);
        this.produtoRepository.saveAndFlush(ProdutoJpa.from(produto));
        assertEquals(1, this.produtoRepository.count());
        final var expectedId = produto.getId();
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'nome' nao deve ser nulo";

        final var input = AtualizaProdutoInput.with(
                expectedId.getValue(),
                null,
                1_000,
                true
        );

        // when
        final var actualException = assertThrows(
                NotificationException.class, () -> useCase.execute(input)
        );

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(produtoGateway).buscaPorId(any());
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

        // when
        final var actualException = assertThrows(
                NotFoundException.class, () -> useCase.execute(input)
        );

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(produtoGateway).buscaPorId(any());
        verify(produtoGateway, never()).atualiza(any());
    }
}
