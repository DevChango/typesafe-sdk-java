package io.github.premocloud.typesafe;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/** @param noul probability that the answer is yes, 0 to 1. There is no separate confidence. */
@JsonIgnoreProperties(ignoreUnknown = true)
public record NoulAnswer(double noul) implements TypeSafeAnswer {
}
