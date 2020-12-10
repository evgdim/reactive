package com.github.evgdim.reactive;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.evgdim.reactive.model.Todo;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.*;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.util.function.Tuple2;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

public class ParallelTests {
    private static WireMockServer wireMockServer = new WireMockServer(options().port(8089));

    @BeforeEach
    public void startMockServer() {
        wireMockServer.start();
    }
    @AfterEach
    public void stopMockServer() {
        wireMockServer.stop();
    }

    @Test
    public void parallelCallsMonoZip() {
        wireMockServer.stubFor(get(urlEqualTo("/todos/1"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody("{ \"userId\": 1, \"id\": 1, \"title\": \"delectus aut autem\", \"completed\": false }")));
        wireMockServer.stubFor(get(urlEqualTo("/todos/2"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody("{ \"userId\": 1, \"id\": 2, \"title\": \"delectus aut autem2\", \"completed\": false }")));

        WebClient webClient = WebClient.builder().baseUrl(wireMockServer.baseUrl()).build();
        Mono<Todo> todoMono1 = webClient.get().uri("/todos/1").retrieve().bodyToMono(Todo.class);
        Mono<Todo> todoMono2 = webClient.get().uri("/todos/2").retrieve().bodyToMono(Todo.class);

        Mono<Tuple2<Todo, Todo>> tupleMono = todoMono1.zipWith(todoMono2);

        StepVerifier.create(tupleMono)
                .expectNextMatches(t -> 1 == t.getT1().getId() && 2 == t.getT2().getId())
        .expectComplete()
        .verify();
    }
}
