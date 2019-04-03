package br.fcv.r2dbc_poc.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static java.lang.Thread.currentThread;
import static java.util.stream.Stream.generate;
import static org.springframework.http.MediaType.APPLICATION_STREAM_JSON_VALUE;
import static reactor.core.publisher.Flux.fromStream;

@RestController
public class GreetingController {

    private static final Duration DEFAULT_DELAY = Duration.ofSeconds(1);

    @GetMapping(value = "/greetings", produces = APPLICATION_STREAM_JSON_VALUE)
    public Flux<Greeting> stream(@RequestParam("delay") final Optional<Duration> optionalDelay) {
        final String id = UUID.randomUUID().toString() + "@" + currentThread().getName();
        final Stream<Greeting> stream = generate(() -> Greeting.builder()
                .id(id)
                .at(Instant.now())
                .source(currentThread().getName())
                .build());
        final Flux<Greeting> flux = fromStream(stream);

        final Duration delay = optionalDelay
            .filter(d -> !d.isNegative())
            .orElse(DEFAULT_DELAY);

        final Flux<Greeting> delayedFlux;
        if (delay.isZero()) {
            delayedFlux = flux;
        } else {
            delayedFlux = flux.delayElements(delay);
        }

        return delayedFlux;
    }

    @GetMapping("/greet")
    public Mono<Greeting> greet(@RequestParam("name") final Optional<String> name) {
        return Mono.just(name.filter(s -> !s.isEmpty()).orElse("World"))
                .map(n -> String.format("Hello %s!", n))
                .map(message -> Greeting.builder()
                        .id(message)
                        .at(Instant.now())
                        .source(currentThread().getName())
                        .build());
    }

}


