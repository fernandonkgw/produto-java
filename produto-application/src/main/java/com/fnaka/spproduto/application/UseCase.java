package com.fnaka.spproduto.application;

public interface UseCase<INPUT, OUTPUT> {

    OUTPUT execute(INPUT input);
}
