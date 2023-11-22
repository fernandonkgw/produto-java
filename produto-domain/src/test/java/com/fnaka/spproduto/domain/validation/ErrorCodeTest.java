package com.fnaka.spproduto.domain.validation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ErrorCodeTest {

    @Test
    void givenErrorCodePRO_001_whenCallsGetCode_thenReturnCode() {
        // given
        final var expectedCode = "PRO-001";
        final var errorCode = ErrorCode.PRO_001;

        // when
        final var actualCode = errorCode.getCode();

        // then
        assertEquals(expectedCode, actualCode);
    }
}