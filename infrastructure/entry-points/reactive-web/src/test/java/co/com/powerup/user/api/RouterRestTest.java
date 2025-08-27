package co.com.powerup.user.api;

import co.com.powerup.user.api.dto.UserDto;
import co.com.powerup.user.api.handler.GlobalExceptionHandler;
import co.com.powerup.user.api.handler.UserHandler;
import co.com.powerup.user.api.helper.ValidationHelper;
import co.com.powerup.user.api.mapper.UserDtoMapperImpl;
import co.com.powerup.user.model.user.UserModel;
import co.com.powerup.user.model.user.exception.EntityNotFoundException;
import co.com.powerup.user.usecase.user.UserUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@org.junit.jupiter.api.extension.ExtendWith(MockitoExtension.class)
class UsersHandlerTest {

    @InjectMocks
    private UserHandler userHandler;

    @Mock
    private UserUseCase userUseCase;

    @Mock
    private ValidationHelper validationHelper;

    @Mock
    private UserDtoMapperImpl userMapper;

    private WebTestClient webTestClient;

    @BeforeEach
    void setup() {
        RouterFunction<?> route = RouterFunctions.route()
                .POST("/api/users", userHandler::saveUser)
                .GET("/api/users/{email}", userHandler::getUserEmail)
                .GET("/api/users", userHandler::getAllUser)
                .PATCH("/api/users/{id}", userHandler::editUser)
                .DELETE("/api/users/{id}", userHandler::deleteUser)
                .build();

        webTestClient = WebTestClient.bindToRouterFunction(route)
                .handlerStrategies(HandlerStrategies.builder()
                        .exceptionHandler(new GlobalExceptionHandler())
                        .build())
                .build();


    }

    @Test
    void saveUserSuccess() {
        UserDto requestDto = new UserDto(
                null, "Steven", "Alejandro", "Palacio", "Lopez", "",
                LocalDate.of(1994, 5, 24), "Calle falsa 123",
                "3001234567", "email.com", new BigDecimal("1994")
        );

        UserModel mappedModel = new UserModel(
                null, "Steven", "Alejandro", "Palacio", "Lopez", "",
                LocalDate.of(1994, 5, 24), "Calle falsa 123",
                "3001234567", "email.com", new BigDecimal("1994")
        );

        UserModel savedModel = new UserModel(
                1L, "Steven", "Alejandro", "Palacio", "Lopez", "",
                LocalDate.of(1994, 5, 24), "Calle falsa 123",
                "3001234567", "email.com", new BigDecimal("1994")
        );

        UserDto responseDto = new UserDto(
                1L, "Steven", "Alejandro", "Palacio", "Lopez", "",
                LocalDate.of(1994, 5, 24), "Calle falsa 123",
                "3001234567", "email.com", new BigDecimal("1994")
        );
        when(validationHelper.validFieldReactive(any(UserDto.class))).thenReturn(Mono.just(requestDto));
        when(userMapper.dtoToModel(requestDto)).thenReturn(mappedModel);
        when(userUseCase.saveUser(mappedModel)).thenReturn(Mono.just(savedModel));
        when(userMapper.modelToDto(savedModel)).thenReturn(responseDto);

        webTestClient.post()
                .uri("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.message").isEqualTo("User created successfully")
                .jsonPath("$.data.email").isEqualTo("email.com");
    }


    @Test
    void getUserEmailSuccess() {
        UserModel model = new UserModel(
                1L, "Steven", "Alejandro", "Palacio", "Lopez", "",
                LocalDate.of(1994, 5, 24), "Calle falsa 123",
                "3001234567", "email.com", new BigDecimal("1994")
        );

        UserDto dto = new UserDto(
                1L, "Steven", "Alejandro", "Palacio", "Lopez", "",
                LocalDate.of(1994, 5, 24), "Calle falsa 123",
                "3001234567", "email.com", new BigDecimal("1994")
        );

        when(userUseCase.getUserByEmail("email.com")).thenReturn(Mono.just(model));
        when(userMapper.modelToDto(model)).thenReturn(dto);


        webTestClient.get()
                .uri("/api/users/email.com")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.message").isEqualTo("User get successfully")
                .jsonPath("$.data.email").isEqualTo("email.com");
    }

    @Test
    void getAllUserASuccess() {

        UserModel model = new UserModel(
                1L, "Steven", "Alejandro", "Palacio", "Lopez", "",
                LocalDate.of(1994, 5, 24), "Calle falsa 123",
                "3001234567", "email.com", new BigDecimal("1994")
        );


        UserModel model1 = new UserModel(
                1L, "Steven", "Alejandro", "Palacio", "Lopez", "",
                LocalDate.of(1994, 5, 24), "Calle falsa 123",
                "3001234567", "email.com", new BigDecimal("1994")
        );


        UserModel model2 = new UserModel(
                1L, "Steven", "Alejandro", "Palacio", "Lopez", "",
                LocalDate.of(1994, 5, 24), "Calle falsa 123",
                "3001234567", "email.com", new BigDecimal("1994")
        );

        UserDto dto = new UserDto(
                1L, "Steven", "Alejandro", "Palacio", "Lopez", "",
                LocalDate.of(1994, 5, 24), "Calle falsa 123",
                "3001234567", "email.com", new BigDecimal("1994")
        );

        UserDto dto1 = new UserDto(
                1L, "Steven", "Alejandro", "Palacio", "Lopez", "",
                LocalDate.of(1994, 5, 24), "Calle falsa 123",
                "3001234567", "email.com", new BigDecimal("1994")
        );


        UserDto dto2 = new UserDto(
                1L, "Steven", "Alejandro", "Palacio", "Lopez", "",
                LocalDate.of(1994, 5, 24), "Calle falsa 123",
                "3001234567", "email.com", new BigDecimal("1994")
        );


        when(userUseCase.getAllUser()).thenReturn(Flux.just(model, model1, model2));
        when(userMapper.modelToDto(model)).thenReturn(dto);
        when(userMapper.modelToDto(model1)).thenReturn(dto1);
        when(userMapper.modelToDto(model2)).thenReturn(dto2);

        webTestClient.get()
                .uri("/api/users")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.message").isEqualTo("Users retrieved successfully")
                .jsonPath("$.data[0].email").isEqualTo(dto.email())
                .jsonPath("$.data[1].email").isEqualTo(dto1.email())
                .jsonPath("$.data[2].email").isEqualTo(dto2.email());

    }

    @Test
    void editUserSuccess() {


        UserModel model = new UserModel(
                1L, "Steven", "Alejandro", "Palacio", "Lopez", "",
                LocalDate.of(1994, 5, 24), "Calle falsa 123",
                "3001234567", "email.com", new BigDecimal("1994")
        );


        UserDto userDto = new UserDto(
                1L, "Steven", "Alejandro", "Palacio", "Lopez", "",
                LocalDate.of(1994, 5, 24), "Calle falsa 123",
                "3001234567", "email.com", new BigDecimal("1994")
        );

        when(validationHelper.validFieldReactive(any(UserDto.class))).thenReturn(Mono.just(userDto));
        when(userMapper.dtoToModel(userDto)).thenReturn(model);
        when(userUseCase.editUser(model, 1L)).thenReturn(Mono.just(model));
        when(userMapper.modelToDto(model)).thenReturn(userDto);

        webTestClient.patch()
                .uri("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(userDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.message").isEqualTo("User edited successfully")
                .jsonPath("$.data.email").isEqualTo(userDto.email());
    }


    @Test
    void editUserNotFound() {


        UserModel model = new UserModel(
                1L, "Steven", "Alejandro", "Palacio", "Lopez", "",
                LocalDate.of(1994, 5, 24), "Calle falsa 123",
                "3001234567", "email.com", new BigDecimal("1994")
        );


        UserDto userDto = new UserDto(
                1L, "Steven", "Alejandro", "Palacio", "Lopez", "",
                LocalDate.of(1994, 5, 24), "Calle falsa 123",
                "3001234567", "email.com", new BigDecimal("1994")
        );

        when(validationHelper.validFieldReactive(any(UserDto.class))).thenReturn(Mono.just(userDto));
        when(userMapper.dtoToModel(userDto)).thenReturn(model);
        when(userUseCase.editUser(model, 1L))
                .thenReturn(Mono.error(new EntityNotFoundException("User not found with id: 1")));

        webTestClient.patch()
                .uri("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(userDto)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.message").isEqualTo("User not found with id: 1")
                .jsonPath("$.status").isEqualTo("BAD_REQUEST")
                .jsonPath("$.error").isEqualTo("RuntimeException");
    }

    @Test
    void getUserNotFound() {

        when(userUseCase.getUserByEmail("email.com"))
                .thenReturn(Mono.error(new EntityNotFoundException("User not found with email: email.com")));

        webTestClient.get()
                .uri("/api/users/email.com")
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.message").isEqualTo("User not found with email: email.com")
                .jsonPath("$.status").isEqualTo("BAD_REQUEST")
                .jsonPath("$.error").isEqualTo("RuntimeException");
    }


    @Test
    void deletedUserNotFound() {

        when(userUseCase.deleteUser(1L))
                .thenReturn(Mono.error(new EntityNotFoundException("User not found with email: email.com")));

        webTestClient.delete()
                .uri("/api/users/1")
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.message").isEqualTo("User not found with email: email.com")
                .jsonPath("$.status").isEqualTo("BAD_REQUEST")
                .jsonPath("$.error").isEqualTo("RuntimeException");
    }


    @Test
    void deletedUser() {

        when(userUseCase.deleteUser(1L))
                .thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/api/users/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.message").isEqualTo("User deleted successfully")
                .jsonPath("$.status").isEqualTo("OK");
    }

}
