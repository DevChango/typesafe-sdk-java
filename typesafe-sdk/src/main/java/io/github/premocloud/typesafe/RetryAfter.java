package io.github.premocloud.typesafe;

import java.net.http.HttpHeaders;
import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

/** Parses {@code retry-after-ms} (preferred) or {@code Retry-After} as seconds or an HTTP date. */
final class RetryAfter {

    private RetryAfter() {
    }

    static Optional<Duration> parse(HttpHeaders headers) {
        return parse(headers, Instant.now());
    }

    static Optional<Duration> parse(HttpHeaders headers, Instant now) {
        Optional<String> millis = headers.firstValue("retry-after-ms");

        if (millis.isPresent()) {
            Optional<Duration> parsed = nonNegativeNumber(millis.get()).map(Duration::ofMillis);

            if (parsed.isPresent()) {
                return parsed;
            }
        }

        Optional<String> raw = headers.firstValue("retry-after");

        if (raw.isEmpty()) {
            return Optional.empty();
        }

        Optional<Duration> seconds = nonNegativeNumber(raw.get()).map(Duration::ofSeconds);

        if (seconds.isPresent()) {
            return seconds;
        }

        try {
            Instant at = ZonedDateTime.parse(raw.get(), DateTimeFormatter.RFC_1123_DATE_TIME).toInstant();
            Duration until = Duration.between(now, at);
            return Optional.of(until.isNegative() ? Duration.ZERO : until);
        } catch (DateTimeParseException e) {
            return Optional.empty();
        }
    }

    private static Optional<Long> nonNegativeNumber(String value) {
        try {
            double parsed = Double.parseDouble(value.trim());
            return parsed >= 0 && Double.isFinite(parsed) ? Optional.of((long) parsed) : Optional.empty();
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }
}
