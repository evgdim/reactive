package com.github.evgdim.reactive;

import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class WebClientExample {
    public static void main(String[] args) throws InterruptedException {
        WebClient client = WebClient.create("http://google.com");
        Mono<String> result = client.get()
                .uri("/")
                .accept(MediaType.TEXT_PLAIN)
                .retrieve().bodyToMono(String.class);
        result.subscribe(res -> {
                    System.out.println(res);
                });
        Thread.sleep(4000);
    }
}
