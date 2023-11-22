package com.fnaka.spproduto.domain.produto;

import com.fnaka.spproduto.domain.validation.ErrorCode;
import com.fnaka.spproduto.domain.validation.DomainError;
import com.fnaka.spproduto.domain.validation.ValidationHandler;
import com.fnaka.spproduto.domain.validation.Validator;

public class ProdutoValidator extends Validator {

    private static final int NOME_MAX_LENGTH = 255;
    private static final int NOME_MIN_LENGTH = 3;

    private final Produto produto;

    public ProdutoValidator(final Produto produto, final ValidationHandler handler) {
        super(handler);
        this.produto = produto;
    }

    @Override
    public void validate() {
        checkNomeConstraints();
        checkPrecoConstraints();
    }

    private void checkNomeConstraints() {
        final var nome = produto.getNome();
        if (nome == null) {
            this.validationHandler().append(DomainError.with(ErrorCode.PRO_001));
            return;
        }
        if (nome.isBlank()) {
            this.validationHandler().append(DomainError.with(ErrorCode.PRO_002));
            return;
        }
        final var length = nome.trim().length();
        if (length > NOME_MAX_LENGTH || length < NOME_MIN_LENGTH) {
//            final var message = "'nome' deve ser entre 3 a 255 characters. Nome informado: %s"
//                    .formatted(nome);
            this.validationHandler().append(DomainError.with(ErrorCode.PRO_003, nome));
        }
    }

    private void checkPrecoConstraints() {
        final var preco = produto.getPreco();
        if (preco == null) {
            this.validationHandler().append(DomainError.with(ErrorCode.PRO_004));
            return;
        }
        if (preco <= 0) {
//            final var message = "'preco' deve ser maior que zero. Preco informado: %s"
//                    .formatted(preco);
            this.validationHandler().append(DomainError.with(ErrorCode.PRO_005, preco));
            return;
        }
    }
}
