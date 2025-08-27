package co.com.powerup.user.r2dbc.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

@Table(name = "users")
@Data
public class UserEntity {

    @Id
    public Long id;

    @Column("first_name")
    String firstName;

    @Column("middle_name")
    String middleName;

    @Column("last_name")
    String lastName;

    @Column("second_last_name")
    String secondLastName;

    @Column("other_last_name")
    String otherLastName;

    @Column("birth_date")
    LocalDate birthDate;

    @Column("address")
    String address;

    @Column("phone_number")
    String phoneNumber;

    @Column("email")
    String email;
    @Column("base_salar")

    BigDecimal baseSalary;
}
