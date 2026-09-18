package io.github.premocloud.typesafe;

import org.jspecify.annotations.Nullable;

import java.net.http.HttpHeaders;
import java.time.Duration;
import java.util.Optional;

/** HTTP 429: the rate limit was exceeded. */
public class TypeSafeRateLimitException extends TypeSafeApiException {

    TypeSafeRateLimitException(int status, @Nullable String body, HttpHeaders headers) {
        super(status, body, headers);
    }

    /** @return the server's requested delay from {@code retry-after-ms} or {@code Retry-After}, when present and valid */
    public Optional<Duration> retryAfter() {
        return RetryAfter.parse(headers());
    }
}
