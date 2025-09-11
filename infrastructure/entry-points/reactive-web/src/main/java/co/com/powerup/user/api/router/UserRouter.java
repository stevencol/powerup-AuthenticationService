package co.com.powerup.user.api.router;

import co.com.powerup.user.api.handler.UserHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.RequestPredicates.PATCH;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;

@Configuration
public class UserRouter {

    @Bean
    public RouterFunction<ServerResponse> routerFunction(UserHandler handler) {
        return route(POST("/users").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::saveUser)
                .andRoute(GET("/users/email/{email}").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::getUserByEmail)
                .andRoute(GET("/users").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::getAllUser)
                .andRoute(PATCH("/users/{id}").and(RequestPredicates.contentType(MediaType.APPLICATION_JSON)), handler::editUser)
                .andRoute(DELETE("/users/{id}"), handler::deleteUser)
                .andRoute(GET("/users/document"), handler::findMyUserByDocumentNumber)
                .andRoute(GET("/users/document/{documentNumber}"), handler::findUserByDocumentNumber);

    }
}


