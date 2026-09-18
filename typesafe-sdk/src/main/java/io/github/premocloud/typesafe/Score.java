package io.github.premocloud.typesafe;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/**
 * Degree along an ordered rubric. Levels are positional, lowest first; the answer is a probability-weighted position.
 *
 * <pre>{@code
 * Score.of("How severe is the problem?", "cosmetic", "degraded", "blocked")
 * Score.of(s -> s.instructions("How urgent?").level("No time pressure").level(l -> l.what("Threatens loss within hours")))
 * }</pre>
 *
 * @param instructions the question, or {@code null}
 * @param criteria     at least two descriptions, one per level from zero
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record Score(@Nullable Object instructions, List<@Nullable Object> criteria) implements TypeSafeQuestion {

    private static final int MIN_LEVELS = 2;

    public static Score of(Object instructions, String... levels) {
        return of(instructions, List.of(levels));
    }

    public static Score of(Object instructions, List<?> levels) {
        Builder builder = builder().instructions(instructions);
        levels.forEach(builder::level);
        return builder.build();
    }

    public static Score of(Consumer<Builder> configure) {
        Builder builder = builder();
        configure.accept(builder);
        return builder.build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private @Nullable Object instructions;
        private final List<@Nullable Object> levels = new ArrayList<>();

        public Builder instructions(Object instructions) {
            this.instructions = instructions;
            return this;
        }

        public Builder level(@Nullable Object description) {
            levels.add(description);
            return this;
        }

        public Builder level(Consumer<Criterion.Builder> configure) {
            return level(Criterion.of(configure));
        }

        public Score build() {
            if (levels.size() < MIN_LEVELS) {
                throw new IllegalStateException("A score question needs at least %d levels, got %d".formatted(MIN_LEVELS, levels.size()));
            }

            return new Score(instructions, Collections.unmodifiableList(new ArrayList<>(levels)));
        }
    }
}
