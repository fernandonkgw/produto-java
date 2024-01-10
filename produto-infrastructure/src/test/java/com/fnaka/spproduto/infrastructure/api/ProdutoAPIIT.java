package com.fnaka.spproduto.infrastructure.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fnaka.spproduto.Fixture;
import com.fnaka.spproduto.IntegrationTest;
import com.fnaka.spproduto.domain.produto.Produto;
import com.fnaka.spproduto.domain.produto.ProdutoID;
import com.fnaka.spproduto.infrastructure.produto.models.AtualizaProdutoRequest;
import com.fnaka.spproduto.infrastructure.produto.models.CriaProdutoRequest;
import com.fnaka.spproduto.infrastructure.produto.persistence.ProdutoJpa;
import com.fnaka.spproduto.infrastructure.produto.persistence.ProdutoRepository;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@IntegrationTest
class ProdutoAPIIT {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Test
    void givenBodyValido_whenCallsCria_thenReturnResponse() throws Exception {
        // given
        final var expectedNome = Fixture.nome();
        final var expectedPreco = Fixture.preco();
        final var expectedEstaAtivo = true;

        final var requestBody = new CriaProdutoRequest(
                expectedNome,
                expectedPreco,
                expectedEstaAtivo
        );

        assertEquals(0, produtoRepository.count());

        // when
        final var request = post("/produtos")
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(requestBody));

        final var response = this.mvc.perform(request)
                .andDo(print());

        // then
        response.andExpect(status().isCreated())
                .andExpect(header().string("Location", notNullValue()))
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(notNullValue()));

        assertEquals(1, produtoRepository.count());
        final String id = JsonPath.read(response.andReturn().getResponse().getContentAsString(), "$.id");
        final var produtoJpa = produtoRepository.findById(id).get();
        assertEquals(expectedNome, produtoJpa.getNome());
        assertEquals(expectedPreco, produtoJpa.getPreco());
        assertEquals(expectedEstaAtivo, produtoJpa.isEstaAtivo());
        assertNotNull(produtoJpa.getCriadoEm());
        assertNotNull(produtoJpa.getAtualizadoEm());
        assertNull(produtoJpa.getRemovidoEm());
    }

    @Test
    void givenNomeInvalido_whenCallsCria_thenReturnNotification() throws Exception {
        // given
        final var expectedPreco = Fixture.preco();
        final var expectedEstaAtivo = true;
        final var expectedErrorMessage = "'nome' nao deve ser nulo";

        final var requestBody = new CriaProdutoRequest(
                null,
                expectedPreco,
                expectedEstaAtivo
        );

        // when
        final var request = post("/produtos")
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(requestBody));

        final var response = this.mvc.perform(request)
                .andDo(print());

        // then
        response.andExpect(status().isBadRequest())
                .andExpect(header().string("Location", nullValue()))
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));
    }

    @Test
    void givenNomeInvalidoAndPrecoZero_whenCallsCria_thenReturnNotification() throws Exception {
        // given
        final var expectedPreco = 0;
        final var expectedEstaAtivo = true;
        final var expectedErrorMessage1 = "'nome' nao deve ser nulo";
        final var expectedErrorMessage2 = "'preco' deve ser maior que zero. Preco informado: 0";

        final var requestBody = new CriaProdutoRequest(
                null,
                expectedPreco,
                expectedEstaAtivo
        );

        // when
        final var request = post("/produtos")
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(requestBody));

        final var response = this.mvc.perform(request)
                .andDo(print());

        // then
        response.andExpect(status().isBadRequest())
                .andExpect(header().string("Location", nullValue()))
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.errors", hasSize(2)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedErrorMessage1)))
                .andExpect(jsonPath("$.errors[1].message", equalTo(expectedErrorMessage2)));
    }

    @Test
    void givenIdValido_whenCallsBuscaPorId_thenReturnProduto() throws Exception {
        // given
        final var expectedNome = Fixture.nome();
        final var expectedPreco = Fixture.preco();
        final var expectedEstaAtivo = true;

        final var produto = Produto.newProduto(expectedNome, expectedPreco, expectedEstaAtivo);
        assertEquals(0, produtoRepository.count());
        produtoRepository.saveAndFlush(ProdutoJpa.from(produto));
        assertEquals(1, produtoRepository.count());
        final var expectedId = produto.getId();

        // when
        final var request = get("/produtos/{id}", expectedId.getValue())
                .accept(APPLICATION_JSON);

        final var response = this.mvc.perform(request)
                .andDo(print());

        // then
        response.andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.id", equalTo(expectedId.getValue())))
                .andExpect(jsonPath("$.nome", equalTo(expectedNome)))
                .andExpect(jsonPath("$.preco", equalTo(expectedPreco)))
                .andExpect(jsonPath("$.estaAtivo", equalTo(expectedEstaAtivo)))
                .andExpect(jsonPath("$.criadoEm", equalTo(produto.getCriadoEm().toString())))
                .andExpect(jsonPath("$.atualizadoEm", equalTo(produto.getAtualizadoEm().toString())));
    }

    @Test
    void givenIdInvalido_whenCallsBuscaPorId_thenThrowsNotFoundException() throws Exception {
        // given
        final var expectedErrorMessage = "Produto com ID 123 nao encontrado";
        final var expectedId = ProdutoID.from("123");

        assertEquals(0, produtoRepository.count());

        // when
        final var request = get("/produtos/{id}", expectedId.getValue())
                .accept(APPLICATION_JSON);

        final var response = this.mvc.perform(request)
                .andDo(print());

        // then
        response.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));
    }

    @Test
    void givenBodyValido_whenCallsAtualizaPorId_thenReturnResponse() throws Exception {
        // given
        final var expectedNome = Fixture.nome();
        final var expectedPreco = Fixture.preco();
        final var expectedEstaAtivo = true;
        final var produto = Produto.newProduto("abcd", 10, false);
        assertEquals(0, produtoRepository.count());
        produtoRepository.saveAndFlush(ProdutoJpa.from(produto));
        assertEquals(1, produtoRepository.count());
        final var expectedId = produto.getId();

        final var requestBody = new AtualizaProdutoRequest(
                expectedNome,
                expectedPreco,
                expectedEstaAtivo
        );

        // when
        final var request = MockMvcRequestBuilders.put("/produtos/{id}", expectedId.getValue())
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(requestBody));

        final var response = this.mvc.perform(request)
                .andDo(print());

        // then
        response.andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.id", equalTo(expectedId.getValue())));

        final var produtoJpa = produtoRepository.findById(expectedId.getValue()).get();
        assertEquals(expectedNome, produtoJpa.getNome());
        assertEquals(expectedPreco, produtoJpa.getPreco());
        assertEquals(expectedEstaAtivo, produtoJpa.isEstaAtivo());
        assertEquals(produto.getCriadoEm(), produtoJpa.getCriadoEm());
        assertTrue(produto.getAtualizadoEm().isBefore(produtoJpa.getAtualizadoEm()));
        assertNull(produtoJpa.getRemovidoEm());
    }

    @Test
    void givenNomeInvalido_whenCallsAtualizaPorId_thenReturnNotification() throws Exception {
        // given
        final var expectedPreco = Fixture.preco();
        final var expectedEstaAtivo = true;
        final var produto = Produto.newProduto(Fixture.nome(), expectedPreco, expectedEstaAtivo);
        assertEquals(0, produtoRepository.count());
        produtoRepository.saveAndFlush(ProdutoJpa.from(produto));
        assertEquals(1, produtoRepository.count());
        final var expectedId = produto.getId();
        final var expectedErrorMessage = "'nome' nao deve ser nulo";

        final var requestBody = new AtualizaProdutoRequest(
                null,
                expectedPreco,
                expectedEstaAtivo
        );

        // when
        final var request = MockMvcRequestBuilders.put("/produtos/{id}", expectedId.getValue())
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(requestBody));

        final var response = this.mvc.perform(request)
                .andDo(print());

        // then
        response.andExpect(status().isBadRequest())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));
    }

    @Test
    void givenParamValidos_whenCallsLista_thenReturnPagination() throws Exception {
        // given
        final var produto = Produto.newProduto("alguma coisa", Fixture.preco(), true);
        assertEquals(0, produtoRepository.count());
        produtoRepository.saveAndFlush(ProdutoJpa.from(produto));
        assertEquals(1, produtoRepository.count());

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTermo = "Alg";
        final var expectedSort = "nome";
        final var expectedDirection = "asc";

        final var expectedItemsCount = 1;
        final var expectedTotal = 1;

        // when
        final var request = get("/produtos")
                .param("page", String.valueOf(expectedPage))
                .param("perPage", String.valueOf(expectedPerPage))
                .param("termo", expectedTermo)
                .param("sort", expectedSort)
                .param("direction", expectedDirection)
                .accept(APPLICATION_JSON);

        final var response = this.mvc.perform(request)
                .andDo(print());

        // then
        response.andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.page", equalTo(expectedPage)))
                .andExpect(jsonPath("$.perPage", equalTo(expectedPerPage)))
                .andExpect(jsonPath("$.total", equalTo(expectedTotal)))
                .andExpect(jsonPath("$.items", hasSize(expectedItemsCount)))
                .andExpect(jsonPath("$.items[0].id", equalTo(produto.getId().getValue())))
                .andExpect(jsonPath("$.items[0].nome", equalTo(produto.getNome())))
                .andExpect(jsonPath("$.items[0].preco", equalTo(produto.getPreco())))
                .andExpect(jsonPath("$.items[0].criadoEm", equalTo(produto.getCriadoEm().toString())));
    }
}