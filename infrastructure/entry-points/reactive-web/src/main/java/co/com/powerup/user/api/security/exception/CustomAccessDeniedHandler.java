package co.com.powerup.user.api.security.exception;

import co.com.powerup.user.api.dto.ApiResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static constants.MessageExceptions.*;

import java.nio.charset.StandardCharsets;

@Component
@AllArgsConstructor
public class CustomAccessDeniedHandler implements ServerAccessDeniedHandler {


    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, AccessDeniedException denied) {

        ApiResponseDto<Object> apiResponse =
                new ApiResponseDto<>(
                        "FORBIDDEN",
                        MSG_FORBIDDEN,
                        HttpStatus.FORBIDDEN
                );

        byte[] bytes;
        try {
            bytes = objectMapper.writeValueAsBytes(apiResponse);
        } catch (Exception e) {
            bytes = (e.getMessage()).getBytes(StandardCharsets.UTF_8);
        }

        exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        return exchange.getResponse().writeWith(
                Mono.just(exchange.getResponse().bufferFactory().wrap(bytes))
        );
    }
}
