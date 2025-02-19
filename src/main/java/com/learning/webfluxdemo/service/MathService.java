package com.learning.webfluxdemo.service;

import com.learning.webfluxdemo.dto.Response;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.IntStream;

@Service
public class MathService {

    public Response findSquare(int input) {
        return new Response(input * input);
    }

    public List<Response> multiplicationTable(int input) {
        return IntStream.range(1, 10)
                .peek(i -> SleepUtil.sleep(1))
                .peek(i -> System.out.println("math-service processing: " + i))
                .mapToObj(i -> new Response(i * input))
                .toList();
    }
}
