package com.fnaka.spproduto.domain.validation;

public enum ErrorCode {
    PRO_001("'nome' nao deve ser nulo"),
    PRO_002("'nome' nao deve ser vazio"),
    PRO_003("'nome' deve ser entre 3 a 255 characters. Nome informado: %s"),
    PRO_004("'preco' nao deve ser nulo"),
    PRO_005("'preco' deve ser maior que zero. Preco informado: %s"),
    PRO_006("%s com ID %s nao encontrado");

    private final String message;

    ErrorCode(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getCode() {
        return this.name().replace("_", "-");
    }
}
