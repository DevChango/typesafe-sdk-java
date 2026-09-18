package io.github.premocloud.typesafe;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jspecify.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Yes/no question. The answer is the probability that the answer is yes.
 *
 * <pre>{@code
 * Noul.of("Does `ticket` convey urgency?")
 * Noul.of(n -> n.instructions("Does `ticket` convey urgency?").whenTrue("Explicitly time-sensitive").whenFalse("No urgency"))
 * }</pre>
 *
 * @param instructions the question, or {@code null} to let the criteria speak for themselves
 * @param criteria     optional descriptions under the keys {@code true} and {@code false}
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record Noul(@Nullable Object instructions, @Nullable Map<String, Object> criteria) implements TypeSafeQuestion {

    public static Noul of(Object instructions) {
        return new Noul(instructions, null);
    }

    public static Noul of(Consumer<Builder> configure) {
        Builder builder = builder();
        configure.accept(builder);
        return builder.build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private @Nullable Object instructions;
        private final Map<String, Object> criteria = new LinkedHashMap<>();

        public Builder instructions(Object instructions) {
            this.instructions = instructions;
            return this;
        }

        public Builder whenTrue(String description) {
            return criterion("true", description);
        }

        public Builder whenTrue(Criterion criterion) {
            return criterion("true", criterion);
        }

        public Builder whenTrue(Consumer<Criterion.Builder> configure) {
            return criterion("true", Criterion.of(configure));
        }

        public Builder whenFalse(String description) {
            return criterion("false", description);
        }

        public Builder whenFalse(Criterion criterion) {
            return criterion("false", criterion);
        }

        public Builder whenFalse(Consumer<Criterion.Builder> configure) {
            return criterion("false", Criterion.of(configure));
        }

        private Builder criterion(String key, Object value) {
            criteria.put(key, value);
            return this;
        }

        public Noul build() {
            return new Noul(instructions, criteria.isEmpty() ? null : Map.copyOf(criteria));
        }
    }
}
