package io.github.premocloud.typesafe;

import org.jspecify.annotations.Nullable;

import java.net.http.HttpHeaders;

/** HTTP 404: the resource was not found. */
public class TypeSafeNotFoundException extends TypeSafeApiException {

    TypeSafeNotFoundException(int status, @Nullable String body, HttpHeaders headers) {
        super(status, body, headers);
    }
}
