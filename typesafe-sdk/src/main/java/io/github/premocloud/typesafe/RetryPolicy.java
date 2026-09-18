package io.github.premocloud.typesafe;

import java.time.Duration;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * When and how the client retries. {@link #DEFAULT} matches the other TypeSafe SDKs: two retries after the first
 * attempt, exponential backoff from 500 ms capped at 5 s with 25 percent jitter, on HTTP 408, 429, and 5xx, honoring
 * {@code Retry-After} up to one minute, and also on connection failures and timeouts.
 *
 * <pre>{@code
 * RetryPolicy.of(r -> r.maxRetries(5).backoffMax(Duration.ofSeconds(20)))
 * RetryPolicy.none()
 * }</pre>
 *
 * @param maxRetries         retries after the initial attempt; 0 disables retries
 * @param backoffInitial     first backoff delay, doubled each retry up to {@code backoffMax}
 * @param backoffMax         cap on the exponential delay
 * @param backoffJitter      fraction of each delay randomly subtracted, 0 to 1
 * @param httpStatuses       statuses that are retried
 * @param respectRetryAfter  whether a server-supplied delay replaces the backoff
 * @param maxRetryAfter      longest server delay honored; longer ones fall back to backoff
 * @param retryConnectionErrors whether connection failures are retried
 * @param retryTimeouts      whether timeouts are retried
 */
public record RetryPolicy(
        int maxRetries,
        Duration backoffInitial,
        Duration backoffMax,
        double backoffJitter,
        Set<Integer> httpStatuses,
        boolean respectRetryAfter,
        Duration maxRetryAfter,
        boolean retryConnectionErrors,
        boolean retryTimeouts
) {
    public static final Set<Integer> DEFAULT_HTTP_STATUSES = Set.copyOf(IntStream.concat(IntStream.of(408, 429), IntStream.range(500, 600))
            .boxed().collect(Collectors.toSet()));

    public static final RetryPolicy DEFAULT = new RetryPolicy(2, Duration.ofMillis(500), Duration.ofSeconds(5), 0.25,
            DEFAULT_HTTP_STATUSES, true, Duration.ofSeconds(60), true, true);

    public RetryPolicy {
        if (maxRetries < 0) {
            throw new IllegalArgumentException("maxRetries must be zero or more");
        }

        if (backoffJitter < 0 || backoffJitter > 1) {
            throw new IllegalArgumentException("backoffJitter must be between 0 and 1");
        }

        httpStatuses = Set.copyOf(httpStatuses);
    }

    public static RetryPolicy none() {
        return DEFAULT.withMaxRetries(0);
    }

    public static RetryPolicy of(Consumer<Builder> configure) {
        Builder builder = new Builder(DEFAULT);
        configure.accept(builder);
        return builder.build();
    }

    public RetryPolicy withMaxRetries(int maxRetries) {
        return new RetryPolicy(maxRetries, backoffInitial, backoffMax, backoffJitter, httpStatuses, respectRetryAfter, maxRetryAfter,
                retryConnectionErrors, retryTimeouts);
    }

    public boolean retriesStatus(int status) {
        return httpStatuses.contains(status);
    }

    public static final class Builder {
        private int maxRetries;
        private Duration backoffInitial;
        private Duration backoffMax;
        private double backoffJitter;
        private Set<Integer> httpStatuses;
        private boolean respectRetryAfter;
        private Duration maxRetryAfter;
        private boolean retryConnectionErrors;
        private boolean retryTimeouts;

        private Builder(RetryPolicy base) {
            maxRetries = base.maxRetries;
            backoffInitial = base.backoffInitial;
            backoffMax = base.backoffMax;
            backoffJitter = base.backoffJitter;
            httpStatuses = base.httpStatuses;
            respectRetryAfter = base.respectRetryAfter;
            maxRetryAfter = base.maxRetryAfter;
            retryConnectionErrors = base.retryConnectionErrors;
            retryTimeouts = base.retryTimeouts;
        }

        public Builder maxRetries(int maxRetries) {
            this.maxRetries = maxRetries;
            return this;
        }

        public Builder backoffInitial(Duration backoffInitial) {
            this.backoffInitial = backoffInitial;
            return this;
        }

        public Builder backoffMax(Duration backoffMax) {
            this.backoffMax = backoffMax;
            return this;
        }

        public Builder backoffJitter(double backoffJitter) {
            this.backoffJitter = backoffJitter;
            return this;
        }

        public Builder httpStatuses(Set<Integer> httpStatuses) {
            this.httpStatuses = httpStatuses;
            return this;
        }

        public Builder respectRetryAfter(boolean respectRetryAfter) {
            this.respectRetryAfter = respectRetryAfter;
            return this;
        }

        public Builder maxRetryAfter(Duration maxRetryAfter) {
            this.maxRetryAfter = maxRetryAfter;
            return this;
        }

        public Builder retryConnectionErrors(boolean retryConnectionErrors) {
            this.retryConnectionErrors = retryConnectionErrors;
            return this;
        }

        public Builder retryTimeouts(boolean retryTimeouts) {
            this.retryTimeouts = retryTimeouts;
            return this;
        }

        public RetryPolicy build() {
            return new RetryPolicy(maxRetries, backoffInitial, backoffMax, backoffJitter, httpStatuses, respectRetryAfter, maxRetryAfter,
                    retryConnectionErrors, retryTimeouts);
        }
    }
}
