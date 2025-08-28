package co.com.powerup.user.api.router;

import co.com.powerup.user.api.dto.UserDto;
import co.com.powerup.user.api.handler.UserHandler;

import io.swagger.v3.oas.annotations.Operation;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
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
    @RouterOperations({
            @RouterOperation(path = "/api/users",
                    produces = {MediaType.APPLICATION_JSON_VALUE},
                    consumes = {MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.POST,
                    beanClass = UserHandler.class,
                    beanMethod = "saveUser",
                    operation =
                    @Operation(
                            operationId = "saveUser",
                            summary = "Create User",
                            tags = {"User"},
                            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                    description = "User to create",
                                    required = true,
                                    content = @io.swagger.v3.oas.annotations.media.Content(
                                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = UserDto.class)
                                    )
                            ),
                            responses = {
                                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "User created successfully"),
                                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request data"),
                                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "User already exists"),
                                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
                            }
                    )),
            @RouterOperation(
                    produces = {MediaType.APPLICATION_JSON_VALUE},
                    consumes = {MediaType.APPLICATION_JSON_VALUE},
                    beanClass = UserHandler.class,
                    beanMethod = "getAllUser",
                    operation = @Operation(
                            operationId = "getAllUser",
                            summary = "Get All Users",
                            tags = {"User"},
                            responses = {
                                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
                                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
                            }
                    )),


    }

    )

    public RouterFunction<ServerResponse> routerFunction(UserHandler handler) {
        return route(POST("/users").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::saveUser)
                .andRoute(GET("/users/{email}").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::getUserEmail)
                .andRoute(GET("/users").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::getAllUser)
                .andRoute(PATCH("/users/{id}").and(RequestPredicates.contentType(MediaType.APPLICATION_JSON)), handler::editUser)
                .andRoute(DELETE("/users/{id}"), handler::deleteUser);
    }
}
