package com.fnaka.spproduto.application.produto.busca;

import com.fnaka.spproduto.application.produto.ProdutoGateway;
import com.fnaka.spproduto.domain.exceptions.NotFoundException;
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
import static org.mockito.Mockito.verify;

@IntegrationTest
class BuscaProdutoPorIdUseCaseIT {

    @Autowired
    private BuscaProdutoPorIdUseCase useCase;

    @Autowired
    private ProdutoRepository produtoRepository;

    @SpyBean
    private ProdutoGateway produtoGateway;

    @Test
    void givenIdValido_whenCallsExecute_thenReturnOutput() {
        // given
        final var expectedNome = Fixture.nome();
        final var expectedPreco = Fixture.preco();
        final var expectedEstaAtivo = true;

        final var produto = Produto.newProduto(expectedNome, expectedPreco, expectedEstaAtivo);
        final var expectedId = produto.getId();

        this.produtoRepository.saveAndFlush(ProdutoJpa.from(produto));
        assertEquals(1, this.produtoRepository.count());

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

        verify(produtoGateway).buscaPorId(any());
    }

    @Test
    void givenIdInvalido_whenCallsExecute_thenThrowsNotFoundException() {
        // given
        final var expectedId = ProdutoID.from("id-invalido");
        final var expectedErrorMessage = "Produto com ID id-invalido nao encontrado";

        // when
        final var actualException = assertThrows(
                NotFoundException.class, () -> useCase.execute(expectedId.getValue())
        );

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getMessage());

        verify(produtoGateway).buscaPorId(any());
    }
}
