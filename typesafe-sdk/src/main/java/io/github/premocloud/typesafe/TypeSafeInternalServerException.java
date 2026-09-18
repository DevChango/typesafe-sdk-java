package io.github.premocloud.typesafe;

import org.jspecify.annotations.Nullable;

import java.net.http.HttpHeaders;

/** HTTP 5xx: the server failed to handle the request. */
public class TypeSafeInternalServerException extends TypeSafeApiException {

    TypeSafeInternalServerException(int status, @Nullable String body, HttpHeaders headers) {
        super(status, body, headers);
    }
}
