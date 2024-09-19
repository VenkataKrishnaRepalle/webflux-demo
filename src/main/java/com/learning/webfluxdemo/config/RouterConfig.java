package com.learning.webfluxdemo.config;

import com.learning.webfluxdemo.dto.InputFailedValidationResponse;
import com.learning.webfluxdemo.exception.InputValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.util.function.BiFunction;

@Configuration
public class RouterConfig {

    @Autowired
    private RequestHandler requestHandler;

    @Bean
    public RouterFunction<ServerResponse> highLevelRouter() {
        return RouterFunctions.route()
                .path("router", this::serverResponseRouterFunction)
                .build();
    }

//    @Bean -- converted public to private. Because router will check in highLevelRouter method first
//    public RouterFunction<ServerResponse> serverResponseRouterFunction() {
//        return RouterFunctions.route()
//                .GET("router/square/{input}", requestHandler::squareHandler)
//                .GET("router/table/{input}", requestHandler::tableHandler)
//                .GET("router/table-stream/{input}", requestHandler::tableStreamHandler)
//                .POST("router/multiply", requestHandler::multiplyHandler)
//                .GET("router/square/{input}/validation", this.requestHandler::squareHandlerWithValidation)
//                .onError(InputValidationException.class, exceptionHandler())
//                .build();
//    }

    private RouterFunction<ServerResponse> serverResponseRouterFunction() {
        return RouterFunctions.route()
                .GET("square/{input}", RequestPredicates.path("*/1?").or(RequestPredicates.path("*/20")), requestHandler::squareHandler)
                .GET("square/{input}", req -> ServerResponse.badRequest().bodyValue("only 10-20 allowed"))
                .GET("table/{input}", requestHandler::tableHandler)
                .GET("table-stream/{input}", requestHandler::tableStreamHandler)
                .POST("multiply", requestHandler::multiplyHandler)
                .GET("square/{input}/validation", this.requestHandler::squareHandlerWithValidation)
                .onError(InputValidationException.class, exceptionHandler())
                .build();
    }

    private BiFunction<Throwable, ServerRequest, Mono<ServerResponse>> exceptionHandler() {
        return (err, req) -> {
            InputValidationException ex = (InputValidationException) err;
            InputFailedValidationResponse response = new InputFailedValidationResponse();
            response.setInput(ex.getInput());
            response.setMessage(ex.getMessage());
            response.setErrorCode(ex.getErrorCode());
            return ServerResponse.badRequest().bodyValue(response);
        };
    }
}
