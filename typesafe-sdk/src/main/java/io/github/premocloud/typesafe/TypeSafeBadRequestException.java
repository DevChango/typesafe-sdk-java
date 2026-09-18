package io.github.premocloud.typesafe;

import org.jspecify.annotations.Nullable;

import java.net.http.HttpHeaders;

/** HTTP 400: the request is invalid. */
public class TypeSafeBadRequestException extends TypeSafeApiException {

    TypeSafeBadRequestException(int status, @Nullable String body, HttpHeaders headers) {
        super(status, body, headers);
    }
}
