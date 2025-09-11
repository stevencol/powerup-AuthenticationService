package co.com.powerup.user.api.config;

import co.com.powerup.user.api.dto.UserDto;
import co.com.powerup.user.api.dto.UserLoginDto;
import co.com.powerup.user.api.handler.LoginHandler;
import co.com.powerup.user.api.handler.UserHandler;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
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
@OpenAPIDefinition(
        info = @Info(
                title = "Auth Service API",
                version = "1.0",
                description = "Api de autenticación y gestión de usuarios"
        ),
        security = {
                @SecurityRequirement(name = "bearerAuth")
        }
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class ApiDocsConfig {
    @Bean
    @RouterOperations({

            @RouterOperation(
                    path = "/users",
                    produces = {MediaType.APPLICATION_JSON_VALUE},
                    consumes = {MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.POST,
                    beanClass = UserHandler.class,
                    beanMethod = "saveUser",
                    operation = @Operation(
                            operationId = "saveUser",
                            summary = "Create User (requires auth)",
                            tags = {"User"},
                            security = {},
                            requestBody = @RequestBody(
                                    required = true,
                                    description = "User to create",
                                    content = @Content(schema = @Schema(implementation = UserDto.class))
                            ),
                            responses = {
                                    @ApiResponse(responseCode = "201", description = "User created successfully"),
                                    @ApiResponse(responseCode = "400", description = "Invalid request data"),
                                    @ApiResponse(responseCode = "409", description = "User already exists"),
                                    @ApiResponse(responseCode = "500", description = "Internal server error")
                            }
                    )
            ),

            @RouterOperation(
                    path = "/users/email/{email}",
                    produces = {MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.GET,
                    beanClass = UserHandler.class,
                    beanMethod = "getUserByEmail",
                    operation = @Operation(
                            operationId = "getUserByEmail",
                            summary = "Get user by email",
                            tags = {"User"},
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "User retrieved successfully"),
                                    @ApiResponse(responseCode = "404", description = "User not found"),
                                    @ApiResponse(responseCode = "500", description = "Internal server error")
                            }
                    )
            ),

            @RouterOperation(
                    path = "/users",
                    produces = {MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.GET,
                    beanClass = UserHandler.class,
                    beanMethod = "getAllUser",
                    operation = @Operation(
                            operationId = "getAllUser",
                            summary = "Get all users",
                            tags = {"User"},
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
                                    @ApiResponse(responseCode = "500", description = "Internal server error")
                            }
                    )
            ),

            @RouterOperation(
                    path = "/users/{id}",
                    produces = {MediaType.APPLICATION_JSON_VALUE},
                    consumes = {MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.PATCH,
                    beanClass = UserHandler.class,
                    beanMethod = "editUser",
                    operation = @Operation(
                            operationId = "editUser",
                            summary = "Edit user by ID",
                            tags = {"User"},
                            requestBody = @RequestBody(
                                    required = true,
                                    description = "User data to update",
                                    content = @Content(schema = @Schema(implementation = UserDto.class))
                            ),
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "User updated successfully"),
                                    @ApiResponse(responseCode = "404", description = "User not found"),
                                    @ApiResponse(responseCode = "400", description = "Invalid request data"),
                                    @ApiResponse(responseCode = "500", description = "Internal server error")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/users/{id}",
                    method = RequestMethod.DELETE,
                    beanClass = UserHandler.class,
                    beanMethod = "deleteUser",
                    operation = @Operation(
                            operationId = "deleteUser",
                            summary = "Delete user by ID",
                            tags = {"User"},
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "User deleted successfully"),
                                    @ApiResponse(responseCode = "404", description = "User not found"),
                                    @ApiResponse(responseCode = "500", description = "Internal server error")
                            }
                    )
            ),

            @RouterOperation(
                    path = "/users/document",
                    produces = {MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.GET,
                    beanClass = UserHandler.class,
                    beanMethod = "findUserByDocumentNumber",
                    operation = @Operation(
                            operationId = "findUserByDocumentNumber",
                            summary = "Get user by document number (requires auth)",
                            tags = {"User"},
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "User retrieved successfully"),
                                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                                    @ApiResponse(responseCode = "404", description = "User not found"),
                                    @ApiResponse(responseCode = "500", description = "Internal server error")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/login",
                    produces = {MediaType.APPLICATION_JSON_VALUE},
                    consumes = {MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.POST,
                    beanClass = LoginHandler.class,
                    beanMethod = "loginUser",
                    operation = @Operation(
                            operationId = "loginUser",
                            summary = "Authenticate user and return JWT",
                            tags = {"Login"},
                            security = {},
                            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                    description = "User credentials",
                                    required = true,
                                    content = @Content(
                                            schema = @Schema(implementation = UserLoginDto.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Login successful, JWT returned"),
                                    @ApiResponse(responseCode = "401", description = "Invalid credentials"),
                                    @ApiResponse(responseCode = "403", description = "Too many failed attempts")
                            }
                    )
            )

    })
    public RouterFunction<ServerResponse> userDocsRoutes() {
        return RouterFunctions.route(request -> false, req -> ServerResponse.ok().build());
    }

}


