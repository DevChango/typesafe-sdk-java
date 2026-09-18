package io.github.premocloud.typesafe;

import org.jspecify.annotations.Nullable;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Per-call overrides for timeout, retry policy, and headers. Anything left unset inherits the client's setting.
 *
 * <pre>{@code
 * client.systemOne(request, RequestOptions.of(o -> o.timeout(Duration.ofSeconds(30)).maxRetries(0)));
 * client.models().list(RequestOptions.of(o -> o.header("X-Trace", traceId)));
 * }</pre>
 *
 * @param timeout     per-attempt timeout, or {@code null} for the client's
 * @param retryPolicy retry policy, or {@code null} for the client's
 * @param headers     headers added to this call; same-named client headers are replaced
 */
public record RequestOptions(@Nullable Duration timeout, @Nullable RetryPolicy retryPolicy, Map<String, String> headers) {

    public static final RequestOptions NONE = new RequestOptions(null, null, Map.of());

    public RequestOptions {
        headers = Map.copyOf(headers);
    }

    public static RequestOptions of(Consumer<Builder> configure) {
        Builder builder = new Builder();
        configure.accept(builder);
        return builder.build();
    }

    public static final class Builder {
        private @Nullable Duration timeout;
        private @Nullable RetryPolicy retryPolicy;
        private final Map<String, String> headers = new LinkedHashMap<>();

        public Builder timeout(Duration timeout) {
            if (timeout.isNegative() || timeout.isZero()) {
                throw new IllegalArgumentException("timeout must be positive");
            }

            this.timeout = timeout;
            return this;
        }

        public Builder retryPolicy(RetryPolicy retryPolicy) {
            this.retryPolicy = retryPolicy;
            return this;
        }

        /** Shorthand for the client's policy with a different retry count; {@code 0} disables retries for this call. */
        public Builder maxRetries(int maxRetries) {
            this.retryPolicy = RetryPolicy.DEFAULT.withMaxRetries(maxRetries);
            return this;
        }

        public Builder header(String name, String value) {
            headers.put(name, value);
            return this;
        }

        public RequestOptions build() {
            return new RequestOptions(timeout, retryPolicy, headers);
        }
    }
}
