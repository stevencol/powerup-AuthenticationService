package co.com.powerup.user.model.user;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class UserModel {

    Long id;
    String firstName;
    String middleName;
    String lastName;
    String secondLastName;
    String otherLastName;
    LocalDate birthDate;
    String address;
    String phoneNumber;
    String email;
    Long rolId;
    BigDecimal baseSalary;
    String documentNumber;
    String password;
}
