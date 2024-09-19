package com.learning.webfluxdemo.controller;

import com.learning.webfluxdemo.dto.Response;
import com.learning.webfluxdemo.exception.InputValidationException;
import com.learning.webfluxdemo.service.ReactiveMathService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("reactive-math")
public class ReactiveMathValidationController {

    @Autowired
    private ReactiveMathService mathService;

    @GetMapping("square/{input}/throw")
    public Mono<Response> findSquare(@PathVariable int input) {
        if (input < 10 || input > 20) {
            throw new InputValidationException(input);
        }
        return this.mathService.findSquare(input);
    }

    @GetMapping("square/{input}/mono-error")
    public Mono<Response> mononError(@PathVariable int input) {
        return Mono.just(input)
                .handle(((integer, sink) -> {
                    if (integer >= 10 && integer <= 20)
                        sink.next(integer);
                    else
                        sink.error(new InputValidationException(integer));
                }))
                .cast(Integer.class)
                .flatMap(this.mathService::findSquare);
    }

    @GetMapping("square/{input}/assignment")
    public Mono<ResponseEntity<Response>> assignment(@PathVariable int input) {
        return Mono.just(input)
                .filter(i -> i >= 10 && i < 20)
                .flatMap(this.mathService::findSquare)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }
}
