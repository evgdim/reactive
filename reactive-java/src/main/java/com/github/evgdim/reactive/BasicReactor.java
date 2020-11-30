package com.github.evgdim.reactive;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Timer;
import java.util.TimerTask;

public class BasicReactor {
    public static void main(String[] args) {
        createFluxFromTimer();
    }

    private static void createFluxFromTimer() {
        Timer timer = new Timer();
        Flux.create(sink -> {
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    System.out.println("timer" + LocalDateTime.now());
                    sink.next("sink" + LocalDateTime.now());
                }
            }, 500, 2000);

        })
                .subscribe(v -> System.out.println(v),
                           err -> System.out.println(err),
                           () -> System.out.println("Done"));
    }
}
