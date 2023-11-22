package com.fnaka.spproduto.infrastructure.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fnaka.spproduto.ControllerTest;
import com.fnaka.spproduto.Fixture;
import com.fnaka.spproduto.application.produto.busca.BuscaProdutoPorIdUseCase;
import com.fnaka.spproduto.application.produto.busca.ProdutoOutput;
import com.fnaka.spproduto.application.produto.cria.CriaProdutoOutput;
import com.fnaka.spproduto.application.produto.cria.CriaProdutoUseCase;
import com.fnaka.spproduto.domain.exceptions.NotFoundException;
import com.fnaka.spproduto.domain.exceptions.NotificationException;
import com.fnaka.spproduto.domain.produto.Produto;
import com.fnaka.spproduto.domain.produto.ProdutoID;
import com.fnaka.spproduto.domain.validation.ErrorCode;
import com.fnaka.spproduto.domain.validation.handler.Notification;
import com.fnaka.spproduto.infrastructure.produto.models.CriaProdutoRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ControllerTest(controllers = ProdutoAPI.class)
class ProdutoAPITest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CriaProdutoUseCase criaProdutoUseCase;

    @MockBean
    private BuscaProdutoPorIdUseCase buscaProdutoPorIdUseCase;

    @Test
    void givenBodyValido_whenCallsCria_thenReturnResponse() throws Exception {
        // given
        final var expectedNome = Fixture.nome();
        final var expectedPreco = Fixture.preco();
        final var expectedEstaAtivo = true;
        final var expectedId = ProdutoID.from("123");

        final var requestBody = new CriaProdutoRequest(
                expectedNome,
                expectedPreco,
                expectedEstaAtivo
        );

        when(criaProdutoUseCase.execute(any()))
                .thenReturn(CriaProdutoOutput.from(expectedId));

        // when
        final var request = MockMvcRequestBuilders.post("/produtos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(requestBody));

        final var response = this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        // then
        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("Location", "/produtos/" + expectedId.getValue()))
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(expectedId.getValue()));
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

        when(criaProdutoUseCase.execute(any()))
                .thenThrow(new NotificationException(Notification.create(ErrorCode.PRO_001)));

        // when
        final var request = MockMvcRequestBuilders.post("/produtos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(requestBody));

        final var response = this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        // then
        response.andExpect(status().isBadRequest())
                .andExpect(header().string("Location", nullValue()))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
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

        when(criaProdutoUseCase.execute(any()))
                .thenThrow(new NotificationException(
                        Notification.create(ErrorCode.PRO_001)
                                .append(ErrorCode.PRO_005, expectedPreco))
                );

        // when
        final var request = MockMvcRequestBuilders.post("/produtos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(requestBody));

        final var response = this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        // then
        response.andExpect(status().isBadRequest())
                .andExpect(header().string("Location", nullValue()))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
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
        final var expectedId = produto.getId();

        when(buscaProdutoPorIdUseCase.execute(any()))
                .thenReturn(ProdutoOutput.from(produto));

        // when
        final var request = MockMvcRequestBuilders.get("/produtos/{id}", expectedId.getValue())
                .accept(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        // then
        response.andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
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

        when(buscaProdutoPorIdUseCase.execute(any()))
                .thenThrow(NotFoundException.with(Produto.class, expectedId));

        // when
        final var request = MockMvcRequestBuilders.get("/produtos/{id}", expectedId.getValue())
                .accept(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        // then
        response.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));
    }
}