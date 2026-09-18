package io.github.premocloud.typesafe;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jspecify.annotations.Nullable;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Pick one option from a named set. The answer carries a probability per option.
 *
 * <pre>{@code
 * Choice.of("What is this ticket about?", "billing", "technical", "other")       // undescribed labels
 * Choice.of(c -> c.instructions("Which category?")
 *         .option("MARKETING", "Promotional content sent to a list")
 *         .option("PHISHING", o -> o.what("Credential theft").notFor("Legitimate requests"))
 *         .option("OTHER"))
 * }</pre>
 *
 * @param instructions the question, or {@code null}
 * @param criteria     labels mapped to a description, a {@link Criterion}, or {@code null} for an undescribed label
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record Choice(@Nullable Object instructions, Map<String, @Nullable Object> criteria) implements TypeSafeQuestion {

    /** Labels without descriptions, as in {@code choice("Which?", {billing: null, technical: null})}. */
    public static Choice of(Object instructions, String... labels) {
        Builder builder = builder().instructions(instructions);

        for (String label : labels) {
            builder.option(label);
        }

        return builder.build();
    }

    public static Choice of(Object instructions, Map<String, ?> criteria) {
        Builder builder = builder().instructions(instructions);
        criteria.forEach(builder::option);
        return builder.build();
    }

    public static Choice of(Consumer<Builder> configure) {
        Builder builder = builder();
        configure.accept(builder);
        return builder.build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private @Nullable Object instructions;
        private final Map<String, @Nullable Object> options = new LinkedHashMap<>();

        public Builder instructions(Object instructions) {
            this.instructions = instructions;
            return this;
        }

        /** An undescribed label. */
        public Builder option(String label) {
            return option(label, (Object) null);
        }

        public Builder option(String label, @Nullable Object description) {
            options.put(label, description);
            return this;
        }

        public Builder option(String label, Consumer<Criterion.Builder> configure) {
            return option(label, Criterion.of(configure));
        }

        public Choice build() {
            if (options.isEmpty()) {
                throw new IllegalStateException("A choice question needs at least one option");
            }

            return new Choice(instructions, Collections.unmodifiableMap(new LinkedHashMap<>(options)));
        }
    }
}
