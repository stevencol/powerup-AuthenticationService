package co.com.powerup.user.api.helper;

import co.com.powerup.user.api.dto.ApiResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;


public class ResponseHelper {

    private ResponseHelper() {
        throw new IllegalStateException("Utility class");
    }

    //Respueesta generica Correcta y Error
    public static <T> Mono<ServerResponse> responseHelper(ApiResponseDto<T> apiResponseDto) {
        return ServerResponse.status(apiResponseDto.status())
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(apiResponseDto));
    }


    //Respuesta generica Error De validacion de campos
    public static Mono<ApiResponseDto<Map<String, Object>>> buildFieldErrorResponse(BindingResult result) {
        Map<String, Object> fields = new HashMap<>();
        result.getFieldErrors().forEach(error -> fields.put(error.getField(), error.getDefaultMessage()));
        ApiResponseDto<Map<String, Object>> apiResponseDto = new ApiResponseDto<>(null, HttpStatus.BAD_REQUEST, "Validation Error", "Invalid fields", fields, null, null);
        return Mono.just(apiResponseDto);
    }

    public static <T> Mono<ApiResponseDto<T>> responseErrorHelper(ApiResponseDto<T> apiResponseDto) {
        return Mono.just(apiResponseDto);
    }


}
