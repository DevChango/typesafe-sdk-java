package io.github.premocloud.typesafe;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;

/**
 * @param choice        highest-probability option
 * @param probabilities probability per option, summing to 1
 * @param confidence    0 to 1, how concentrated the distribution is
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ChoiceAnswer(String choice, Map<String, Double> probabilities, double confidence) implements TypeSafeAnswer {
}
