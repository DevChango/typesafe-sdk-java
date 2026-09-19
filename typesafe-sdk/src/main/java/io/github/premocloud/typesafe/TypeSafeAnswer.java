package io.github.premocloud.typesafe;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/** One answer, keyed in {@link TypeSafeResponse#answers()} by the question id. Prefer the typed accessors on the response. */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = NoulAnswer.class, name = "noul"),
        @JsonSubTypes.Type(value = ChoiceAnswer.class, name = "choice"),
        @JsonSubTypes.Type(value = ScoreAnswer.class, name = "score")
})
public sealed interface TypeSafeAnswer permits NoulAnswer, ChoiceAnswer, ScoreAnswer {

    /** Rejects a missing or null answer field so a malformed response cannot read as a real value (0, null). */
    static <T> T required(T value, String answerType, String field) {
        if (value == null) {
            throw new IllegalArgumentException("%s answer is missing '%s'".formatted(answerType, field));
        }

        return value;
    }
}
