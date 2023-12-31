package com.fnaka.spproduto.infrastructure.api;

import com.fnaka.spproduto.application.Pagination;
import com.fnaka.spproduto.infrastructure.produto.models.AtualizaProdutoRequest;
import com.fnaka.spproduto.infrastructure.produto.models.CriaProdutoRequest;
import com.fnaka.spproduto.infrastructure.produto.models.ListaProdutoResponse;
import com.fnaka.spproduto.infrastructure.produto.models.ProdutoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequestMapping(value = "produtos")
@Tag(name = "Produto")
public interface ProdutoAPI {

    @PostMapping(
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Cria um novo produto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Um erro de validação foi lançado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    ResponseEntity<?> cria(@RequestBody CriaProdutoRequest body);

    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Busca um produto pelo seu identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    ProdutoResponse buscaPorId(@PathVariable String id);

    @PutMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Atualiza um produto pelo seu identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Um erro de validação foi lançado"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    ResponseEntity<?> atualizaPorId(@PathVariable String id, @RequestBody AtualizaProdutoRequest body);

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Lista todos os produtos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produtos listados com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    Pagination<ListaProdutoResponse> lista(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "perPage", required = false, defaultValue = "10") int perPage,
            @RequestParam(name = "termo", required = false, defaultValue = "") String termo,
            @RequestParam(name = "sort", required = false, defaultValue = "nome") String sort,
            @RequestParam(name = "direction", required = false, defaultValue = "asc") String direction
    );
}
