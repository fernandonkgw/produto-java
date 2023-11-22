package com.fnaka.spproduto.application.produto.atualiza;

import com.fnaka.spproduto.domain.exceptions.NotFoundException;
import com.fnaka.spproduto.domain.exceptions.NotificationException;
import com.fnaka.spproduto.domain.produto.Produto;
import com.fnaka.spproduto.application.produto.ProdutoGateway;
import com.fnaka.spproduto.domain.produto.ProdutoID;
import com.fnaka.spproduto.domain.validation.handler.Notification;

import java.util.Objects;

public class DefaultAtualizaProdutoUseCase extends AtualizaProdutoUseCase {

    private final ProdutoGateway produtoGateway;

    public DefaultAtualizaProdutoUseCase(final ProdutoGateway produtoGateway) {
        this.produtoGateway = Objects.requireNonNull(produtoGateway);
    }

    @Override
    public AtualizaProdutoOutput execute(final AtualizaProdutoInput input) {
        final var id = ProdutoID.from(input.id());
        final var nome = input.nome();
        final var preco = input.preco();
        final var estaAtivo = input.estaAtivo();

        final var produto = this.produtoGateway.buscaPorId(id)
                .orElseThrow(() -> NotFoundException.with(Produto.class, id));

        final var notification = Notification.create();
        produto.atualiza(nome, preco, estaAtivo)
                .validate(notification);

        if (notification.hasErrors()) {
            final var mensagem = "Nao foi possivel atualizar o agregado Produto com ID %s"
                    .formatted(id.getValue());
            throw new NotificationException(mensagem, notification);
        }

        return AtualizaProdutoOutput.from(produtoGateway.atualiza(produto));
    }
}
