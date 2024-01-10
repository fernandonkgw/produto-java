package com.fnaka.spproduto.application.produto.cria;

import com.fnaka.spproduto.domain.exceptions.NotificationException;
import com.fnaka.spproduto.domain.produto.Produto;
import com.fnaka.spproduto.application.produto.ProdutoGateway;
import com.fnaka.spproduto.domain.validation.handler.Notification;

public class DefaultCriaProdutoUseCase extends CriaProdutoUseCase {

    private final ProdutoGateway produtoGateway;

    public DefaultCriaProdutoUseCase(final ProdutoGateway produtoGateway) {
        this.produtoGateway = produtoGateway;
    }

    @Override
    public CriaProdutoUseCase.Output execute(final CriaProdutoUseCase.Input input) {
        final var nome = input.nome();
        final var preco = input.preco();
        final var estaAtivo = input.estaAtivo();

        final var produto = Produto.newProduto(nome, preco, estaAtivo);

        final var notification = Notification.create();
        produto.validate(notification);

        if (notification.hasErrors()) {
            throw new NotificationException("Nao foi possivel criar o agregado Produto", notification);
        }

        return CriaProdutoUseCase.Output.from(this.produtoGateway.cria(produto));
    }
}
