package io.github.premocloud.typesafe;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.jspecify.annotations.Nullable;

/** One judgment over the request's state: a {@link Noul}, {@link Choice}, or {@link Score}. */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Noul.class, name = "noul"),
        @JsonSubTypes.Type(value = Choice.class, name = "choice"),
        @JsonSubTypes.Type(value = Score.class, name = "score")
})
public sealed interface TypeSafeQuestion permits Noul, Choice, Score {

    /** The question as text, a structured object such as {@code {"question": ..., "focus": ...}}, or {@code null}. */
    @Nullable Object instructions();
}
