package io.github.premocloud.typesafe;

import org.jspecify.annotations.Nullable;

/** The request or its response could not be delivered: DNS, TLS, refused or dropped connection. */
public class TypeSafeConnectionException extends TypeSafeException {

    public TypeSafeConnectionException(String message, @Nullable Throwable cause) {
        super(message, cause);
    }
}
