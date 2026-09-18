package io.github.premocloud.typesafe;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TypeSafeUsage(
        @JsonProperty("input_tokens") long inputTokens,
        @JsonProperty("output_tokens") long outputTokens
) {
}
