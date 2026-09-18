# Changelog

## 0.1.0 - 2026-09-18

Community library published under `io.github.premo-cloud`; not affiliated with TypeSafe AI.

- `TypeSafeClient` over `java.net.http` with a builder, `fromEnvironment()`, and `systemOne` taking either `(state, questions)` as in the other SDKs, a `TypeSafeRequest`, or a request configurer.
- Question types `Noul`, `Choice`, and `Score` with `of(instructions, criteria)` factories and `of(builder -> ...)` configurers; `Criterion` for structured descriptions; undescribed labels and optional instructions as the API allows.
- Sealed `TypeSafeAnswer` hierarchy and typed accessors on `TypeSafeResponse`.
- Retries matching the other SDKs (`RetryPolicy.DEFAULT`: 2 retries, 500 ms to 5 s backoff with jitter, on 408/429/5xx and connection failures, honoring `Retry-After`).
- Status-specific `TypeSafeApiException` subclasses with extracted messages and request ids; `TypeSafeConnectionException` and `TypeSafeTimeoutException`.
- `RequestOptions` for per-call timeout, retry, and header overrides on `systemOne` and `models().list()`.
- `client.models().list()`, SDK identification headers, extra default headers, and `TYPESAFE_BASE_URL` / `TYPESAFE_DEFAULT_MODEL` environment fallbacks.
- `CriteriaQuestionSet` for generating one question per user-defined criterion.
- Spring Boot starter exposing a `TypeSafeClient` bean from `typesafe.*` properties.
