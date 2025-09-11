package co.com.powerup.user.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;


@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserDto(Long id,
                      @JsonProperty("firstName")
                      @NotBlank
                      @NotNull
                      String firstName,
                      String middleName,
                      @NotBlank
                      @NotNull
                      String lastName,
                      String secondLastName,
                      String otherLastName,
                      @NotNull
                      @JsonFormat(pattern = "yyyy-MM-dd")
                      LocalDate birthDate,
                      @NotBlank
                      @NotNull
                      String address,
                      @NotBlank
                      @NotNull
                      @Pattern(regexp = "(3\\d{9}|\\+573\\d{9})", message = "{telephone.pattern}")
                      String phoneNumber,
                      @NotNull
                      @Email
                      String email,
                      @PositiveOrZero
                      @Max(value = 15000000, message = "El salario no puede superar los 15.000.000")
                      BigDecimal baseSalary,
                      @JsonProperty("documentNumber")
                      String documentNumber,
                      @NotNull
                      String password

) {


}
