package co.com.powerup.user.model.user;

import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
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
    BigDecimal baseSalary;
}
