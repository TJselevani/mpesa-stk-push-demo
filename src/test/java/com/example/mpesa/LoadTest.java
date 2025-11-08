package com.example.mpesa;

import com.example.mpesa.dto.StkPushRequest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@SpringBootTest
public class LoadTest {

    private final WebClient client = WebClient.builder()
            .baseUrl("http://localhost:8080")
            .build();

    @Test
    void testConcurrentStkPushRequests() {
        StkPushRequest request = new StkPushRequest();
        request.setPhoneNumber("254712345678");
        request.setAccountReference("LoadTest");
        request.setDescription("Testing load performance");
        request.setAmount(1);

        Flux.range(1, 100) // 100 concurrent users
                .flatMap(i -> client.post()
                        .uri("/api/mpesa/pay")
                        .bodyValue(request)
                        .retrieve()
                        .bodyToMono(String.class)
                        .timeout(Duration.ofSeconds(5))
                        .onErrorResume(e -> Mono.just("error: " + e.getMessage())))
                .doOnNext(System.out::println)
                .blockLast();
    }
}
