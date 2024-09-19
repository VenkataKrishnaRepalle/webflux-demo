package com.learning.webfluxdemo;

import com.learning.webfluxdemo.dto.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class Lec01GetSingleResponseTest extends BaseTest {

    @Autowired
    private WebClient webClient;

    @Test
    void blockTest() {
        Response response = this.webClient.get()
                .uri("reactive-math/square/{input}", 5)
                .retrieve()
                .bodyToMono(Response.class)
                .block();

        System.out.println(response);
    }

    @Test
    void stepVerifierTest() {
        Mono<Response> responseMono = this.webClient.get()
                .uri("reactive-math/square/{input}", 5)
                .retrieve()
                .bodyToMono(Response.class);

        StepVerifier.create(responseMono)
                .expectNextMatches(r -> r.getOutput() == 25)
                .verifyComplete();
    }

    @Test
    void multiplicationTableTest() {
        Flux<Response> responseFlux = this.webClient.get()
                .uri("/reactive-math/table/{input}", 5)
                .retrieve()
                .bodyToFlux(Response.class)
                .doOnNext(System.out::println);

        StepVerifier.create(responseFlux)
                .expectNextCount(10)
                .verifyComplete();
    }

    @Test
    void multiplicationTableStreamTest() {
        Flux<Response> responseFlux = this.webClient.get()
                .uri("/reactive-math/table/{input}/stream", 5)
                .retrieve()
                .bodyToFlux(Response.class)
                .doOnNext(System.out::println);

        StepVerifier.create(responseFlux)
                .expectNextCount(10)
                .verifyComplete();
    }
}
