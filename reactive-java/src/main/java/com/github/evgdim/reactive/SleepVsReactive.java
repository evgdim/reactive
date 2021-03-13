package com.github.evgdim.reactive;

import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.stream.IntStream;

public class SleepVsReactive {
    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 100_000; i++) {
            //threadSleep();
            delay();
        }

        Thread.sleep(100_000);
    }

    private static void threadSleep() {
        new Thread(() -> {
            try {
                Thread.sleep(10000);
                System.out.println("Finished " + Thread.currentThread().getName());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private static void delay() {
        Mono.just("test")
                .delayElement(Duration.ofMillis(10000))
                .subscribe(r -> System.out.println(r + " Finished " + Thread.currentThread().getName()));
    }
}
