package io.github.premocloud.typesafe;

import org.jspecify.annotations.Nullable;

import java.net.http.HttpHeaders;

/** HTTP 401: authentication failed. */
public class TypeSafeAuthenticationException extends TypeSafeApiException {

    TypeSafeAuthenticationException(int status, @Nullable String body, HttpHeaders headers) {
        super(status, body, headers);
    }
}
