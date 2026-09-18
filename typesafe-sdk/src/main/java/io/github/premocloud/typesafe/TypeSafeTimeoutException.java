package io.github.premocloud.typesafe;

import org.jspecify.annotations.Nullable;

import java.time.Duration;

/** The full response did not arrive within the configured timeout. A kind of connection failure. */
public class TypeSafeTimeoutException extends TypeSafeConnectionException {

    private final Duration timeout;

    public TypeSafeTimeoutException(Duration timeout, @Nullable Throwable cause) {
        super("Request timed out after %dms.".formatted(timeout.toMillis()), cause);
        this.timeout = timeout;
    }

    public Duration timeout() {
        return timeout;
    }
}
