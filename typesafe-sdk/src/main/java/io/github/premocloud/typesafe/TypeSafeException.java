package io.github.premocloud.typesafe;

import org.jspecify.annotations.Nullable;

/** Base class for every error the SDK raises. */
public class TypeSafeException extends RuntimeException {

    public TypeSafeException(String message) {
        super(message);
    }

    public TypeSafeException(String message, @Nullable Throwable cause) {
        super(message, cause);
    }
}
