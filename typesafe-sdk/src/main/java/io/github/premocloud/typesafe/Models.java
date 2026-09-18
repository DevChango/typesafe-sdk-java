package io.github.premocloud.typesafe;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/** The models available to the account, reached through {@link TypeSafeClient#models()}. */
public final class Models {

    static final String PATH = "/v1/models";

    @JsonIgnoreProperties(ignoreUnknown = true)
    record Wire(List<ModelCard> models) {
    }

    private final TypeSafeClient client;

    Models(TypeSafeClient client) {
        this.client = client;
    }

    public List<ModelCard> list() {
        return list(RequestOptions.NONE);
    }

    public List<ModelCard> list(RequestOptions options) {
        Wire wire = client.get(PATH, Wire.class, options);

        if (wire.models() == null) {
            throw new TypeSafeException("Models response did not contain a models list");
        }

        return List.copyOf(wire.models());
    }
}
