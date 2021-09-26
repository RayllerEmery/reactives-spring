package fluxandmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class FluxAndMonoTest {

    @Test
    public void fluxTest() {

        //scenario
        var flux =
                Flux.just("Spring", "SpringBoot", "Reactive Spring")
                        .concatWith(Flux.error(new RuntimeException("Error Occurred.")))
                        .concatWith(Flux.just("After error"))
                        .log();

        //validation
        flux.subscribe(System.out::println,
                (e) -> System.err.println("Exception is: " + e),
                () -> System.out.println("Completed"));
    }

    @Test
    public void fluxTestElements_withoutError() {

        //scenario
        var flux =
                Flux.just("Spring", "SpringBoot", "Reactive Spring")
                        .log();

        //validation
        StepVerifier.create(flux)
                .expectNext("Spring")
                .expectNext("SpringBoot")
                .expectNext("Reactive Spring")
                .verifyComplete();
    }

    @Test
    public void fluxTestElements_withError() {

        //scenario
        var flux = Flux.just("Spring", "SpringBoot", "Reactive Spring")
                .concatWith(Flux.error(new RuntimeException("Error Occurred.")))
                .log();

        //validation
        StepVerifier.create(flux)
                .expectNext("Spring")
                .expectNext("SpringBoot")
                .expectNext("Reactive Spring")
                .expectErrorMessage("Error Occurred.")
                .verify();
    }

    @Test
    public void fluxTestElementsCount_withError() {

        //scenario
        var flux = Flux.just("Spring", "SpringBoot", "Reactive Spring")
                .concatWith(Flux.error(new RuntimeException("Error Occurred.")))
                .log();

        //validation
        StepVerifier.create(flux)
                .expectNextCount(3)
                .expectErrorMessage("Error Occurred.")
                .verify();
    }

    @Test
    public void monoTest() {

        //scenario
        var mono = Mono.just("Spring");

        //validation
        StepVerifier.create(mono.log())
                .expectNext("Spring")
                .verifyComplete();
    }

    @Test
    public void monoTest_withError() {

        //validation
        StepVerifier.create(Mono.error(new RuntimeException("Error Occurred.")).log())
                .expectError(RuntimeException.class)
                .verify();
    }

}
