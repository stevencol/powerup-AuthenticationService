package co.com.powerup.user.api.handler;

import co.com.powerup.user.api.dto.ApiResponseDto;
import co.com.powerup.user.api.dto.UserLoginDto;
import co.com.powerup.user.api.security.service.LoginAttemptService;
import co.com.powerup.user.api.security.service.LoginService;
import exceptions.LoginAttemptException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static constants.MessageExceptions.*;
import static co.com.powerup.user.api.helper.ResponseHelper.responseHelper;
import static constants.MessagesInfo.MSG_OPERATION_SUCCESS;


@Component
@Slf4j
@AllArgsConstructor
public class LoginHandler {
    private final LoginService loginService;

    private final LoginAttemptService loginAttemptService;

    public Mono<ServerResponse> loginUser(ServerRequest request) {
        log.info("Iniciando loginUser");
        return request.bodyToMono(UserLoginDto.class).flatMap(userLogin -> {
            if (loginAttemptService.isBlocked(userLogin.email())) {
                return Mono.error(() -> new LoginAttemptException(MSG_FORBIDDEN));
            }
            return loginService.createTokenUser(Mono.just(userLogin))
                    .doOnError(throwable ->
                            loginAttemptService.recordAttempt(userLogin.email())
                    );
        }).flatMap(authResponse -> responseHelper(new ApiResponseDto<>(HttpStatus.OK, authResponse, MSG_OPERATION_SUCCESS)));
    }
}
