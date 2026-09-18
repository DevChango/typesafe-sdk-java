package io.github.premocloud.typesafe;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/** One model available to the account, from {@code GET /v1/models}. */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ModelCard(String name, String description, @JsonProperty("release_date") String releaseDate) {
}
