package me.rayll.reactivesspring.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@WebFluxTest
class FluxAndMonoControllerTest {

    @Autowired
    private WebTestClient client;

    @Test
    public void flux_approach1() {

        //scenario
        var integerFlux =
                client.get().uri("/flux")
                        .accept(MediaType.APPLICATION_JSON)
                        .exchange()
                        .expectStatus().isOk()
                        .returnResult(Integer.class)
                        .getResponseBody();

        //validation
        StepVerifier.create(integerFlux)
                .expectSubscription()
                .expectNext(1)
                .expectNext(2)
                .expectNext(3)
                .expectNext(4)
                .verifyComplete();
    }

    @Test
    public void flux_approach2() {

        //validation
        client.get().uri("/flux")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Integer.class)
                .hasSize(4);
    }

    @Test
    public void flux_approach3() {

        //scenario
        List<Integer> expectedList = Arrays.asList(1, 2, 3, 4);

        var result = client.get().uri("/flux")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Integer.class)
                .returnResult();

        //validation
        assertEquals(expectedList, result.getResponseBody());

    }

    @Test
    public void flux_approach4() {

        //scenario
        List<Integer> expectedList = Arrays.asList(1, 2, 3, 4);

        //validation
        var result = client.get().uri("/flux")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Integer.class)
                .consumeWith(listEntityExchangeResult -> assertEquals(
                        expectedList, listEntityExchangeResult.getResponseBody()
                ));

    }

    @Test
    public void fluxStream() {

        //scenario
        var fluxlong= client.get().uri("/fluxstream")
                .accept(MediaType.APPLICATION_NDJSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(long.class)
                .getResponseBody();

        //validation
        StepVerifier.create(fluxlong)
                .expectNext(0L)
                .expectNext(1L)
                .expectNext(2L)
                .expectNext(3L)
                .thenCancel()
                .verify();
    }

    @Test
    public void mono() {

        //scenario
        var expectValue = Integer.valueOf(1);

        //validation
        client.get().uri("/mono")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Integer.class)
                .consumeWith(response -> assertEquals(expectValue, response.getResponseBody()));
    }
}