package com.fnaka.spproduto.infrastructure.api.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.List;
import java.util.Locale;

@Component
public class LocaleResolver extends AcceptHeaderLocaleResolver {

    private static final Locale DEFAULT_LOCALE = new Locale("pt", "BR");
    private static final List<Locale> ACCEPTED_LOCALES = List.of(DEFAULT_LOCALE, new Locale("en"));

    @Override
    public Locale resolveLocale(final HttpServletRequest request) {
        final var acceptLanguageHeader = request.getHeader("Accept-Language");

        if (acceptLanguageHeader == null || acceptLanguageHeader.isBlank() || "*".equals(acceptLanguageHeader.trim())) {
            return DEFAULT_LOCALE;
        }

        final var list = Locale.LanguageRange.parse(acceptLanguageHeader);

        final var locale = Locale.lookup(list, ACCEPTED_LOCALES);

        return locale == null ? DEFAULT_LOCALE : locale;

    }
}
