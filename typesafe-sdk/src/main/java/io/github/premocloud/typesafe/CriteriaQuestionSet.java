package io.github.premocloud.typesafe;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Molds a request from a list of caller-defined criteria. The criteria go into the state under {@code criteria[]} so
 * each generated question can point at its own entry by path, and the subject being judged goes under a caller-named key.
 *
 * <pre>{@code
 * TypeSafeRequest request = CriteriaQuestionSet
 *     .over("document", documentState, rules, Rule::id, (rule, path) -> Noul.of("Does `document` fall under the rule at %s?".formatted(path)))
 *     .noul("any_rule", n -> n.instructions("Taking every rule in `criteria` together, does any apply to `document`?"))
 *     .build();
 * }</pre>
 */
public final class CriteriaQuestionSet {

    private static final String CRITERIA_KEY = "criteria";

    private CriteriaQuestionSet() {
    }

    /**
     * @param subjectKey  state key the subject lives under, e.g. "document"
     * @param subject     the thing being judged
     * @param criteria    one entry per criterion, serialized as-is into the state
     * @param questionKey criterion to answer key, usually the criterion id
     * @param askAbout    criterion and its backticked state path, e.g. {@code `criteria[2]`}, to the question to ask
     * @return a builder holding the state and one noul per criterion; add summary questions, then {@code build()}
     */
    public static <C> TypeSafeRequest.Builder over(String subjectKey, Object subject, List<C> criteria,
                                                   Function<C, String> questionKey,
                                                   BiFunction<C, String, Noul> askAbout) {
        TypeSafeRequest.Builder builder = TypeSafeRequest.builder()
                .state(Map.of(CRITERIA_KEY, criteria, subjectKey, subject));

        for (int i = 0; i < criteria.size(); i++) {
            C criterion = criteria.get(i);
            builder.question(questionKey.apply(criterion), askAbout.apply(criterion, "`%s[%d]`".formatted(CRITERIA_KEY, i)));
        }

        return builder;
    }
}
