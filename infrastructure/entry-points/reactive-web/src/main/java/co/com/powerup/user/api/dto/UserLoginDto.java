package co.com.powerup.user.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;


@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserLoginDto(
        @NotNull
        @Email
        String email,

        @NotNull
        String password

) {


}
