package com.github.evgdim.reactive;

import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

public class ColdVsHotPublishers {
    public static void main(String[] args) throws InterruptedException {
        //coldPublisher();
        //coldCounter();

        hotPublisher();
    }

    private static void hotPublisher() throws InterruptedException {
        Flux<Integer> range1to10 = Flux.range(1, 10);
        ConnectableFlux<Integer> published = range1to10
                .delayElements(Duration.ofMillis(500))
                .publish();

        published.subscribe(i -> System.out.println("subscriber 1: " +i));
        published.connect();

        Thread.sleep(2000);
        published.subscribe(i -> System.out.println("subscriber 2: " +i));

        Thread.sleep(6000);
    }

    private static void coldCounter() {
        AtomicInteger counter = new AtomicInteger();
        Flux<Object> sinkCounter = Flux.generate(sink -> sink.next(counter.incrementAndGet()));
        sinkCounter.take(5).subscribe(c -> System.out.println("run 1: " + c));
        sinkCounter.take(5).subscribe(c -> System.out.println("run 2: " + c));
    }

    private static void coldPublisher() {
        Flux<Integer> range1to10 = Flux.range(1, 10);
        range1to10.subscribe(i -> System.out.println("run 1: " +i));
        range1to10.subscribe(i -> System.out.println("run 2: " +i));
    }
}
