package co.com.powerup.user.api.router;


import co.com.powerup.user.api.handler.LoginHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class LoginRouter {

    @Bean
    public RouterFunction<ServerResponse> loginRoutes(LoginHandler handler) {
        return route(POST("/login").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::loginUser);
    }

}
