package io.github.premocloud.typesafe;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

/**
 * @param choice        highest-probability option
 * @param probabilities probability per option, summing to 1
 * @param confidence    0 to 1, how concentrated the distribution is
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ChoiceAnswer(String choice, Map<String, Double> probabilities, double confidence) implements TypeSafeAnswer {

    /** Jackson entry point: a choice answer missing any of its fields is malformed. */
    @JsonCreator
    ChoiceAnswer(
            @JsonProperty("choice") String choice,
            @JsonProperty("probabilities") Map<String, Double> probabilities,
            @JsonProperty("confidence") Double confidence,
            @JsonProperty("type") String ignoredType
    ) {
        this(TypeSafeAnswer.required(choice, "choice", "choice"),
                TypeSafeAnswer.required(probabilities, "choice", "probabilities"),
                TypeSafeAnswer.required(confidence, "choice", "confidence").doubleValue());
    }
}
