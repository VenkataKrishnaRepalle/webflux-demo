package com.learning.webfluxdemo;

import com.learning.webfluxdemo.dto.InputFailedValidationResponse;
import com.learning.webfluxdemo.dto.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class Lec04ExchangeRequest extends BaseTest {

    @Autowired
    private WebClient webClient;

    @Test
    void badRequestTest() {
        Mono<Object> responseMono = webClient.get()
                .uri("reactive-math/square/{input}/throw", 10)
                .exchangeToMono(this::exchange)
                .doOnNext(System.out::println)
                .doOnError(err -> System.out.println(err.getMessage()));

        StepVerifier.create(responseMono)
                .expectNextCount(1)
                .verifyComplete();
    }

    private Mono<Object> exchange(ClientResponse clientResponse) {
        if (clientResponse.statusCode().value() == 400) {
            return clientResponse.bodyToMono(InputFailedValidationResponse.class);
        } else {
            return clientResponse.bodyToMono(Response.class);
        }
    }
}
