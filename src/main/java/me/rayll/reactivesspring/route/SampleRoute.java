package me.rayll.reactivesspring.route;

import me.rayll.reactivesspring.handler.SampleHandlerFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class SampleRoute {

    @Bean
    public RouterFunction<ServerResponse> route(SampleHandlerFunction handler) {

        return RouterFunctions
                .route(GET("/functional/flux").and(accept(MediaType.APPLICATION_JSON)), r -> handler.flux(r))
                .andRoute(GET("/functional/mono").and(accept(MediaType.APPLICATION_JSON)), r -> handler.mono(r));
    }
}