package co.com.powerup.user.api.security.service;

import co.com.powerup.user.api.dto.UserLoginDto;
import co.com.powerup.user.api.security.jwt.JwtProvider;
import co.com.powerup.user.usecase.user.UserUseCase;
import lombok.AllArgsConstructor;


import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@AllArgsConstructor
public class LoginService {


    private final ReactiveAuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final UserUseCase userUseCase;

    public Mono<String> createTokenUser(Mono<UserLoginDto> userLogin) {


        return userLogin
                .flatMap(user -> {
                    UsernamePasswordAuthenticationToken userSession = new UsernamePasswordAuthenticationToken(user.email(), user.password());
                    return authenticationManager.authenticate(userSession)
                            .flatMap(auth -> {
                                log.info("Authentication Success");
                                return userUseCase.findUserByEmail(user.email()).flatMap(
                                        userModel -> {
                                            String token = jwtProvider.generateToken(auth, userModel);
                                            return Mono.just(token);
                                        }
                                );

                            });
                });
    }
}
