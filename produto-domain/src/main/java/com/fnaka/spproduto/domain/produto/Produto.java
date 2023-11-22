package com.fnaka.spproduto.domain.produto;

import com.fnaka.spproduto.domain.AggregateRoot;
import com.fnaka.spproduto.domain.utils.InstantUtils;
import com.fnaka.spproduto.domain.validation.ValidationHandler;

import java.time.Instant;

public class Produto extends AggregateRoot<ProdutoID> {

    private String nome;
    private Integer preco;
    private boolean estaAtivo;
    private final Instant criadoEm;
    private Instant atualizadoEm;
    private Instant removidoEm;

    private Produto(
            final ProdutoID id,
            final String nome,
            final Integer preco,
            final boolean estaAtivo,
            final Instant criadoEm,
            final Instant atualizadoEm,
            final Instant removidoEm
    ) {
        super(id);
        this.nome = nome;
        this.preco = preco;
        this.estaAtivo = estaAtivo;
        this.criadoEm = criadoEm;
        this.atualizadoEm = atualizadoEm;
        this.removidoEm = removidoEm;
    }

    public static Produto newProduto(
            final String nome,
            final Integer preco,
            final boolean estaAtivo
    ) {
        final var id = ProdutoID.unique();
        final var agora = InstantUtils.now();
        final var removidoEm = estaAtivo ? null : agora;
        return new Produto(id, nome, preco, estaAtivo, agora, agora, removidoEm);
    }

    public static Produto with(
            final ProdutoID id,
            final String nome,
            final Integer preco,
            final boolean estaAtivo,
            final Instant criadoEm,
            final Instant atualizadoEm,
            final Instant removidoEm
    ) {
        return new Produto(id, nome, preco, estaAtivo, criadoEm, atualizadoEm, removidoEm);
    }

    public Produto copy() {
        return new Produto(
                getId(),
                getNome(),
                getPreco(),
                isEstaAtivo(),
                getCriadoEm(),
                getAtualizadoEm(),
                getRemovidoEm()
        );
    }

    @Override
    public void validate(ValidationHandler handler) {
        new ProdutoValidator(this, handler).validate();
    }

    public Produto atualiza(
            final String nome,
            final Integer preco,
            final boolean estaAtivo
    ) {
        this.nome = nome;
        this.preco = preco;
        this.estaAtivo = estaAtivo;
        final var agora = InstantUtils.now();
        this.atualizadoEm = agora;
        this.removidoEm = estaAtivo ? null : agora;
        return this;
    }

    public String getNome() {
        return nome;
    }

    public Integer getPreco() {
        return preco;
    }

    public boolean isEstaAtivo() {
        return estaAtivo;
    }

    public Instant getCriadoEm() {
        return criadoEm;
    }

    public Instant getAtualizadoEm() {
        return atualizadoEm;
    }

    public Instant getRemovidoEm() {
        return removidoEm;
    }
}
