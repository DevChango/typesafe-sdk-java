package io.github.premocloud.typesafe;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/** @param noul probability that the answer is yes, 0 to 1. There is no separate confidence. */
@JsonIgnoreProperties(ignoreUnknown = true)
public record NoulAnswer(double noul) implements TypeSafeAnswer {

    /** Jackson entry point: a noul answer whose value is missing or null is malformed, not 0. */
    @JsonCreator
    NoulAnswer(@JsonProperty("noul") Double noul) {
        this(TypeSafeAnswer.required(noul, "noul", "noul").doubleValue());
    }
}
