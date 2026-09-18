package io.github.premocloud.typesafe;

import org.jspecify.annotations.Nullable;

import java.net.http.HttpHeaders;

/** HTTP 403: access is denied. */
public class TypeSafePermissionDeniedException extends TypeSafeApiException {

    TypeSafePermissionDeniedException(int status, @Nullable String body, HttpHeaders headers) {
        super(status, body, headers);
    }
}
