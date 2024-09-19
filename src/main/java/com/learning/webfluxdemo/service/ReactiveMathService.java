package com.learning.webfluxdemo.service;

import com.learning.webfluxdemo.dto.MultiplyRequestDto;
import com.learning.webfluxdemo.dto.Response;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
public class ReactiveMathService {

    public Mono<Response> findSquare(int input) {
        return Mono.fromSupplier(() -> input * input)
                .map(Response::new);
    }

    public Flux<Response> multiplicationTable(int input) {
        return Flux.range(1, 10)
                .delayElements(Duration.ofSeconds(1))
//                .doOnNext(i -> SleepUtil.sleep(1))
                .doOnNext(i -> System.out.println("reactive-math-service processsing: " + i))
                .map(i -> new Response(i * input));
    }

    public Mono<Response> multiply(Mono<MultiplyRequestDto> dtoMono) {
        return dtoMono
                .map(dto -> dto.getFirstNumber() * dto.getSecondNumber())
                .map(Response::new);
    }
}
