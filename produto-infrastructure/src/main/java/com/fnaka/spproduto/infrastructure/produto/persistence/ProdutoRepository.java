package com.fnaka.spproduto.infrastructure.produto.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<ProdutoJpa, String> {

    Page<ProdutoJpa> findAll(Specification<ProdutoJpa> whereClause, Pageable page);
}
