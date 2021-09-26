package me.rayll.reactivesspring.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
public class FluxAndMonoController {

    //Client will block until receive all data from server, using JSON content,
    //but on the application still non-blocking on server-side
    @GetMapping("/flux")
    public Flux<Integer> returnFlux() {
        return Flux.just(1, 2, 3, 4)
                .delayElements(Duration.ofSeconds(1))
                .log();
    }

    //Here, the client will consume it as it receive data from server side
    @GetMapping(value = "/fluxstream", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<Long> returnFluxStream() {
        return Flux.interval(Duration.ofSeconds(1))
                .log();
    }

    @GetMapping("/mono")
    public Mono<Integer> returnMono() {
        return Mono.just(1)
                .log();
    }
}
