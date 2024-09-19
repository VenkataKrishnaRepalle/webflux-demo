package com.learning.webfluxdemo.config;

import com.learning.webfluxdemo.dto.InputFailedValidationResponse;
import com.learning.webfluxdemo.dto.MultiplyRequestDto;
import com.learning.webfluxdemo.dto.Response;
import com.learning.webfluxdemo.exception.InputValidationException;
import com.learning.webfluxdemo.service.ReactiveMathService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Service
public class RequestHandler {

    @Autowired
    private ReactiveMathService mathService;

    public Mono<ServerResponse> squareHandler(ServerRequest serverRequest) {
        int input = Integer.valueOf(serverRequest.pathVariable("input"));
        var response = this.mathService.findSquare(input);
        return ServerResponse.ok().body(response, Response.class);
    }

    public Mono<ServerResponse> tableHandler(ServerRequest serverRequest) {
        int input = Integer.valueOf(serverRequest.pathVariable("input"));
        var response = this.mathService.multiplicationTable(input);
        return ServerResponse.ok().body(response, Response.class);
    }

    public Mono<ServerResponse> tableStreamHandler(ServerRequest serverRequest) {
        int input = Integer.valueOf(serverRequest.pathVariable("input"));
        var response = this.mathService.multiplicationTable(input);
        return ServerResponse.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(response, Response.class);
    }

    public Mono<ServerResponse> multiplyHandler(ServerRequest serverRequest) {
        Mono<MultiplyRequestDto> requestDtoMono = serverRequest.bodyToMono(MultiplyRequestDto.class);
        var response = this.mathService.multiply(requestDtoMono);
        return ServerResponse.ok()
                .body(response, Response.class);
    }

    public Mono<ServerResponse> squareHandlerWithValidation(ServerRequest serverRequest) {
        int input = Integer.valueOf(serverRequest.pathVariable("input"));
        if(input < 10 || input > 20) {
            return Mono.error(new InputValidationException(input));
        }
        var response = this.mathService.findSquare(input);
        return ServerResponse.ok().body(response, Response.class);
    }
}
