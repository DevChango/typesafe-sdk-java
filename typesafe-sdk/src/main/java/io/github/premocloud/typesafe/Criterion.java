package io.github.premocloud.typesafe;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Structured description of one answer option or level. Use it instead of a plain string when an option needs
 * exclusions or examples to be told apart from its neighbours.
 *
 * <pre>{@code
 * Criterion.of("Credential theft or impersonation")
 * Criterion.of(c -> c.what("Credential theft").notFor("Legitimate requests from a known counterparty"))
 * }</pre>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record Criterion(
        String what,
        @JsonProperty("not_for") @Nullable String notFor,
        @Nullable List<String> examples
) {
    public static Criterion of(String what) {
        return new Criterion(what, null, null);
    }

    public static Criterion of(Consumer<Builder> configure) {
        Builder builder = new Builder();
        configure.accept(builder);
        return builder.build();
    }

    public Criterion notFor(String notFor) {
        return new Criterion(what, notFor, examples);
    }

    public Criterion examples(String... examples) {
        return new Criterion(what, notFor, List.of(examples));
    }

    public static final class Builder {
        private @Nullable String what;
        private @Nullable String notFor;
        private @Nullable List<String> examples;

        public Builder what(String what) {
            this.what = what;
            return this;
        }

        public Builder notFor(String notFor) {
            this.notFor = notFor;
            return this;
        }

        public Builder examples(String... examples) {
            this.examples = List.of(examples);
            return this;
        }

        public Criterion build() {
            if (Objects.isNull(what)) {
                throw new IllegalStateException("A criterion needs a description of what it covers");
            }

            return new Criterion(what, notFor, examples);
        }
    }
}
