package com.fnaka.spproduto.infrastructure.produto.persistence;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class ProdutoEstaAtivoConverter implements AttributeConverter<Boolean, String> {

        @Override
        public String convertToDatabaseColumn(Boolean estaAtivo) {
            if (estaAtivo == null) return null;
            return estaAtivo ? "S" : "N";
        }

        @Override
        public Boolean convertToEntityAttribute(String dbData) {
            if (dbData == null) return null;
            return dbData.equals("S");
        }
}
