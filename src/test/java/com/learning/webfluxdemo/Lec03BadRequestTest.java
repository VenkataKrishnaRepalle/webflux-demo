package com.learning.webfluxdemo;

import com.learning.webfluxdemo.dto.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class Lec03BadRequestTest extends BaseTest {

    @Autowired
    private WebClient webClient;

    @Test
    public void badRequestTest() {
        Mono<Response> response = webClient.get()
                .uri("reactive-math/square/{input}/throw", 5)
                .retrieve()
                .bodyToMono(Response.class)
                .doOnNext(System.out::println)
                .doOnError(err -> System.out.println(err.getMessage()));

        StepVerifier.create(response)
                .verifyError(WebClientResponseException.BadRequest.class);
    }

}
