package io.github.premocloud.typesafe;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/** Minimal in-process stand-in for api.typesafe.ai: records each request and replies with a queued response. */
final class StubTypeSafeServer implements AutoCloseable {

    record Recorded(String method, String path, Headers headers, String body) {
    }

    record Reply(int status, String body, Map<String, String> headers, long delayMs) {
    }

    private final HttpServer server;
    private final List<Recorded> recorded = new ArrayList<>();
    private final List<Reply> replies = new ArrayList<>();

    StubTypeSafeServer() throws IOException {
        server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        server.createContext("/", exchange -> {
            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            recorded.add(new Recorded(exchange.getRequestMethod(), exchange.getRequestURI().getPath(), exchange.getRequestHeaders(), body));
            Reply reply = replies.isEmpty() ? new Reply(500, "{\"error\":\"no reply queued\"}", Map.of(), 0) : replies.remove(0);

            if (reply.delayMs() > 0) {
                try {
                    Thread.sleep(reply.delayMs());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            byte[] bytes = reply.body().getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().add("Content-Type", "application/json");
            reply.headers().forEach((name, value) -> exchange.getResponseHeaders().add(name, value));
            exchange.sendResponseHeaders(reply.status(), bytes.length);

            try (OutputStream out = exchange.getResponseBody()) {
                out.write(bytes);
            }
        });
        server.start();
    }

    String baseUrl() {
        return "http://127.0.0.1:" + server.getAddress().getPort();
    }

    void reply(int status, String body) {
        replies.add(new Reply(status, body, Map.of(), 0));
    }

    void reply(int status, String body, Map<String, String> headers) {
        replies.add(new Reply(status, body, headers, 0));
    }

    void replyAfter(long delayMs, int status, String body) {
        replies.add(new Reply(status, body, Map.of(), delayMs));
    }

    List<Recorded> recorded() {
        return recorded;
    }

    @Override
    public void close() {
        server.stop(0);
    }
}
