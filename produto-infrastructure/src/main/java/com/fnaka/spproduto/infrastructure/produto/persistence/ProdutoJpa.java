package com.fnaka.spproduto.infrastructure.produto.persistence;

import com.fnaka.spproduto.domain.produto.Produto;
import com.fnaka.spproduto.domain.produto.ProdutoID;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.Objects;

@Entity(name = "Produto")
@Table(name = "produtos")
public class ProdutoJpa {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "preco")
    private Integer preco;

    @Column(name = "status")
    @Convert(converter = ProdutoEstaAtivoConverter.class)
    private Boolean estaAtivo;

    @Column(name = "criado_em")
    private Instant criadoEm;

    @Column(name = "atualizado_em")
    private Instant atualizadoEm;

    @Column(name = "removido_em")
    private Instant removidoEm;

    public ProdutoJpa() {
    }

    private ProdutoJpa(
            String id,
            String nome,
            Integer preco,
            Boolean estaAtivo,
            Instant criadoEm,
            Instant atualizadoEm,
            Instant removidoEm
    ) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
        this.estaAtivo = estaAtivo;
        this.criadoEm = criadoEm;
        this.atualizadoEm = atualizadoEm;
        this.removidoEm = removidoEm;
    }

    public static ProdutoJpa from(final Produto produto) {
        return new ProdutoJpa(
                produto.getId().getValue(),
                produto.getNome(),
                produto.getPreco(),
                produto.isEstaAtivo(),
                produto.getCriadoEm(),
                produto.getAtualizadoEm(),
                produto.getRemovidoEm().orElse(null)
        );
    }

    public Produto toAggregate() {
        return Produto.with(
                ProdutoID.from(getId()),
                getNome(),
                getPreco(),
                isEstaAtivo(),
                getCriadoEm(),
                getAtualizadoEm(),
                getRemovidoEm()
        );
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getPreco() {
        return preco;
    }

    public void setPreco(Integer preco) {
        this.preco = preco;
    }

    public Boolean isEstaAtivo() {
        return estaAtivo;
    }

    public void setEstaAtivo(Boolean estaAtivo) {
        this.estaAtivo = estaAtivo;
    }

    public Instant getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(Instant criadoEm) {
        this.criadoEm = criadoEm;
    }

    public Instant getAtualizadoEm() {
        return atualizadoEm;
    }

    public void setAtualizadoEm(Instant atualizadoEm) {
        this.atualizadoEm = atualizadoEm;
    }

    public Instant getRemovidoEm() {
        return removidoEm;
    }

    public void setRemovidoEm(Instant removidoEm) {
        this.removidoEm = removidoEm;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final ProdutoJpa that = (ProdutoJpa) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
