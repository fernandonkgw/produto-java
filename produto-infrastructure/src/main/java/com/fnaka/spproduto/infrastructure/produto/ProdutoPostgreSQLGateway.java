package com.fnaka.spproduto.infrastructure.produto;

import com.fnaka.spproduto.application.Pagination;
import com.fnaka.spproduto.application.produto.ProdutoGateway;
import com.fnaka.spproduto.application.produto.lista.ProdutoSearchQuery;
import com.fnaka.spproduto.domain.produto.Produto;
import com.fnaka.spproduto.domain.produto.ProdutoID;
import com.fnaka.spproduto.infrastructure.produto.persistence.ProdutoJpa;
import com.fnaka.spproduto.infrastructure.produto.persistence.ProdutoRepository;
import com.fnaka.spproduto.infrastructure.utils.SpecificationUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
public class ProdutoPostgreSQLGateway implements ProdutoGateway {

    private final ProdutoRepository repository;

    public ProdutoPostgreSQLGateway(final ProdutoRepository repository) {
        this.repository = Objects.requireNonNull(repository);
    }

    @Override
    public Produto cria(final Produto produto) {
        return save(produto);
    }

    @Override
    public Optional<Produto> buscaPorId(final ProdutoID id) {
        return repository.findById(id.getValue())
                .map(ProdutoJpa::toAggregate);
    }

    @Override
    public Produto atualiza(final Produto produto) {
        return save(produto);
    }

    @Override
    public Pagination<Produto> lista(final ProdutoSearchQuery query) {
        final var page = PageRequest.of(
                query.page(),
                query.perPage(),
                Sort.by(Sort.Direction.fromString(query.direction()), query.sort())
        );

        final var where = Optional.ofNullable(query.termo())
                .filter(str -> !str.isBlank())
                .map(this::termoSpecification)
                .orElse(null);

        final var pageResult =
                this.repository.findAll(where, page);

        return new Pagination<>(
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.map(ProdutoJpa::toAggregate).toList()
        );
    }

    private Produto save(final Produto produto) {
        return this.repository.save(ProdutoJpa.from(produto))
                .toAggregate();
    }

    private Specification<ProdutoJpa> termoSpecification(final String termo) {
        return SpecificationUtils.like("nome", termo);
    }
}
