package co.com.powerup.user.r2dbc.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "roles")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RolEntity {
    @Id
    @Column("id")
    private Long id;
    @Column("name")
    private String name;
    @Column("description")
    private String description;

}
