package co.com.powerup.user.model.user.model;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RoleModel {

    private Long id;
    private String name;
    private String description;

}
