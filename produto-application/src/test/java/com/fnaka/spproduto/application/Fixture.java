package com.fnaka.spproduto.application;

import com.github.javafaker.Faker;

public final class Fixture {

    private static final Faker FAKER = new Faker();

    public static String nome() {
        return FAKER.book().title();
    }

    public static Integer preco() {
        return FAKER.number().numberBetween(1, 1_000_000);
    }
}
