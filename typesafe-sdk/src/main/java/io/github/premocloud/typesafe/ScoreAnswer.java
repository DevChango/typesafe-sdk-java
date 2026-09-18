package io.github.premocloud.typesafe;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;

/**
 * @param score         probability-weighted position from 0 to the top level index
 * @param probabilities probability per level index (keys are the index as a string)
 * @param confidence    0 to 1, how concentrated the distribution is
 * @param legend        level index back to the description that was sent
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ScoreAnswer(
        double score,
        Map<String, Double> probabilities,
        double confidence,
        Map<String, String> legend
) implements TypeSafeAnswer {
}
