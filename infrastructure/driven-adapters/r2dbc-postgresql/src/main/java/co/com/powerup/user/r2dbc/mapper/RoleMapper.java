package co.com.powerup.user.r2dbc.mapper;

import co.com.powerup.user.model.user.model.RoleModel;
import co.com.powerup.user.r2dbc.model.RolEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.springframework.stereotype.Component;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
@Component
public interface RoleMapper {

    RoleModel toModel(RolEntity role);
}
