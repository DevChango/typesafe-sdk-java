package io.github.premocloud.typesafe;

import org.junit.jupiter.api.Test;

import java.net.http.HttpHeaders;
import java.time.Duration;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RetryPolicyTest {

    @Test
    void defaultsMatchTheOtherSdks() {
        RetryPolicy policy = RetryPolicy.DEFAULT;

        assertEquals(2, policy.maxRetries());
        assertEquals(Duration.ofMillis(500), policy.backoffInitial());
        assertEquals(Duration.ofSeconds(5), policy.backoffMax());
        assertEquals(0.25, policy.backoffJitter());
        assertTrue(policy.retriesStatus(408));
        assertTrue(policy.retriesStatus(429));
        assertTrue(policy.retriesStatus(500));
        assertTrue(policy.retriesStatus(599));
        assertFalse(policy.retriesStatus(400));
        assertFalse(policy.retriesStatus(404));
        assertTrue(policy.respectRetryAfter());
        assertEquals(Duration.ofSeconds(60), policy.maxRetryAfter());
        assertEquals(0, RetryPolicy.none().maxRetries());
    }

    @Test
    void builderStartsFromDefaultsAndValidates() {
        RetryPolicy policy = RetryPolicy.of(r -> r.maxRetries(5).httpStatuses(Set.of(503)));

        assertEquals(5, policy.maxRetries());
        assertEquals(Duration.ofMillis(500), policy.backoffInitial());
        assertTrue(policy.retriesStatus(503));
        assertFalse(policy.retriesStatus(500));
        assertThrows(IllegalArgumentException.class, () -> RetryPolicy.of(r -> r.maxRetries(-1)));
        assertThrows(IllegalArgumentException.class, () -> RetryPolicy.of(r -> r.backoffJitter(1.5)));
    }

    @Test
    void retryAfterPrefersMillisThenSecondsThenHttpDate() {
        Instant now = Instant.parse("2026-09-18T12:00:00Z");

        assertEquals(Optional.of(Duration.ofMillis(250)), RetryAfter.parse(headers("retry-after-ms", "250", "retry-after", "9"), now));
        assertEquals(Optional.of(Duration.ofSeconds(9)), RetryAfter.parse(headers("retry-after-ms", "nope", "retry-after", "9"), now));
        assertEquals(Optional.of(Duration.ofSeconds(30)), RetryAfter.parse(headers("retry-after", "Fri, 18 Sep 2026 12:00:30 GMT"), now));
        assertEquals(Optional.of(Duration.ZERO), RetryAfter.parse(headers("retry-after", "Fri, 18 Sep 2026 11:00:00 GMT"), now));
        assertEquals(Optional.empty(), RetryAfter.parse(headers("retry-after", "-3"), now));
        assertEquals(Optional.empty(), RetryAfter.parse(headers("retry-after", "soon"), now));
        assertEquals(Optional.empty(), RetryAfter.parse(headers(), now));
    }

    private static HttpHeaders headers(String... pairs) {
        Map<String, List<String>> map = new LinkedHashMap<>();

        for (int i = 0; i < pairs.length; i += 2) {
            map.put(pairs[i], List.of(pairs[i + 1]));
        }

        return HttpHeaders.of(map, (a, b) -> true);
    }
}
