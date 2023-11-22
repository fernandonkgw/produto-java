package com.fnaka.spproduto.domain.exceptions;

import com.fnaka.spproduto.domain.validation.handler.Notification;

public class NotificationException extends DomainException {
    public NotificationException(final String aMessage, final Notification notification) {
        super(aMessage, notification.getErrors());
    }

    public NotificationException(final Notification notification) {
        this("", notification);
    }
}
