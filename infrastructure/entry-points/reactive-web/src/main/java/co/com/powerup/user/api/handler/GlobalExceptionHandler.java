package co.com.powerup.user.api.handler;

import co.com.powerup.user.api.dto.ApiResponseDto;
import co.com.powerup.user.api.helper.ResponseHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import exceptions.EntityNotFoundException;
import exceptions.InvalidTokenException;
import exceptions.LoginAttemptException;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.*;
import reactor.core.publisher.Mono;


import java.util.Optional;

@Component
@Order(-2)
public class GlobalExceptionHandler implements WebExceptionHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        var response = exchange.getResponse();
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ApiResponseDto<?> body;


        switch (ex) {
            case WebExchangeBindException bindEx -> {
                status = HttpStatus.BAD_REQUEST;
                body = ResponseHelper.buildFieldErrorResponse(bindEx.getBindingResult()).block();
            }
            case DuplicateKeyException dupEx -> {
                status = HttpStatus.BAD_REQUEST;
                body = new ApiResponseDto<>("Datos duplicados", dupEx.getMostSpecificCause().toString(), status);
            }
            case ServerWebInputException inputEx -> {
                status = HttpStatus.BAD_REQUEST;
                String param = Optional.ofNullable(inputEx.getMethodParameter())
                        .map(MethodParameter::getParameterName)
                        .orElse("Unknown");


                body = new ApiResponseDto<>(inputEx.getMessage(), param, status);
            }
            case EntityNotFoundException entityNotFoundEx -> {
                status = HttpStatus.NOT_FOUND;
                body = new ApiResponseDto<>("Not Found", entityNotFoundEx.getMessage(), status);
            }
            case MethodNotAllowedException methodEx -> {
                status = HttpStatus.METHOD_NOT_ALLOWED;
                body = new ApiResponseDto<>("Método no permitido",
                        "Solo se permiten: " + methodEx.getSupportedMethods(),
                        status);
            }
            case ResponseStatusException resEx -> {
                status = HttpStatus.valueOf(resEx.getStatusCode().value());
                body = new ApiResponseDto<>(
                        resEx.getReason() != null ? resEx.getReason() : "Error en la petición",
                        "Ruta no encontrada",
                        status
                );
            }case InvalidTokenException invalidTokenEx -> {
                status = HttpStatus.FORBIDDEN;
                body = new ApiResponseDto<>("Token inválid", invalidTokenEx.getMessage(), status);
            }case LoginAttemptException loginAttemptEx -> {
                status = HttpStatus.FORBIDDEN;
                body = new ApiResponseDto<>("Acceso denegado", loginAttemptEx.getMessage(), status);
            }

            default -> body = new ApiResponseDto<>(ex.getLocalizedMessage(), "Error no controlado", status);

        }
        response.setStatusCode(status);

        try {
            byte[] bytes = objectMapper.writeValueAsBytes(body);
            return response.writeWith(Mono.just(response.bufferFactory().wrap(bytes)));
        } catch (Exception e) {
            return response.setComplete();
        }
    }


}
