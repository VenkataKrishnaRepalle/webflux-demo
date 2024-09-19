package com.learning.webfluxdemo;

import com.learning.webfluxdemo.dto.MultiplyRequestDto;
import com.learning.webfluxdemo.dto.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class Lec06AttributesTest extends BaseTest {

    @Autowired
    private WebClient webClient;

    @Test
    void headersTest() {
        Mono<Response> responseMono = this.webClient
                .post()
                .uri("reactive-math/multiply")
                .bodyValue(buildRequestDto(5, 7))
                .attribute("auth", "oauth")
                .retrieve()
                .bodyToMono(Response.class)
                .doOnNext(System.out::println);

        StepVerifier.create(responseMono)
                .expectNextCount(1)
                .verifyComplete();
    }

    private MultiplyRequestDto buildRequestDto(int a, int b) {
        MultiplyRequestDto dto = new MultiplyRequestDto();
        dto.setFirstNumber(a);
        dto.setSecondNumber(b);
        return dto;
    }
}
