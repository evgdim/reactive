package com.github.evgdim.reactive;

import com.github.evgdim.reactive.model.Todo;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.util.function.Tuple2;

import java.time.Duration;

public class ParallelTests {
    @Test
    public void parallelCallsMonoZip() {
        WebClient webClient = WebClient.builder().baseUrl("https://jsonplaceholder.typicode.com").build();
        Mono<Todo> todoMono1 = webClient.get().uri("/todos/1").retrieve().bodyToMono(Todo.class);
        Mono<Todo> todoMono2 = webClient.get().uri("/todos/2").retrieve().bodyToMono(Todo.class);

        Mono<Tuple2<Todo, Todo>> tupleMono = todoMono1.zipWith(todoMono2);

        StepVerifier.create(tupleMono)
                .expectNextMatches(t -> 1 == t.getT1().getId() && 2 == t.getT2().getId())
        .expectComplete()
        .verify();
    }
}
