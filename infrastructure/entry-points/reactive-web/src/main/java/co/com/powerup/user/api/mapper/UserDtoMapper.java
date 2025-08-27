package co.com.powerup.user.api.mapper;

import co.com.powerup.user.api.dto.UserDto;
import co.com.powerup.user.model.user.UserModel;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.springframework.stereotype.Component;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
@Component
public abstract class UserDtoMapper {

    public abstract UserDto modelToDto(UserModel userModel);

    public abstract UserModel dtoToModel(UserDto userDto);
}
