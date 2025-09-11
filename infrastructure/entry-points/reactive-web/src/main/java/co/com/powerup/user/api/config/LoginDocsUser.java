package co.com.powerup.user.api.config;

import co.com.powerup.user.api.dto.UserLoginDto;
import co.com.powerup.user.api.handler.LoginHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class LoginDocsUser {
    @Bean

    public RouterFunction<ServerResponse> loginDocsRoutes() {
        return RouterFunctions.route(request -> false, req -> ServerResponse.ok().build());
    }


}
