package io.github.premocloud.typesafe;

import org.jspecify.annotations.Nullable;

import java.net.http.HttpHeaders;

/** HTTP 422: request validation failed. */
public class TypeSafeUnprocessableEntityException extends TypeSafeApiException {

    TypeSafeUnprocessableEntityException(int status, @Nullable String body, HttpHeaders headers) {
        super(status, body, headers);
    }
}
