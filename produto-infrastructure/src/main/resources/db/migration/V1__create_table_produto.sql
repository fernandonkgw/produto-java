CREATE TABLE produtos
(
    id            VARCHAR(36)  NOT NULL PRIMARY KEY,
    nome          VARCHAR(255) NOT NULL,
    preco         NUMERIC(10)  NOT NULL,
    status        CHAR(1)      NOT NULL,
    criado_em     TIMESTAMP    NOT NULL,
    atualizado_em TIMESTAMP    NOT NULL,
    removido_em   TIMESTAMP
);