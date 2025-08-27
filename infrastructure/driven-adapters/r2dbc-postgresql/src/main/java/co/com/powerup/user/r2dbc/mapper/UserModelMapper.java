package co.com.powerup.user.r2dbc.mapper;


import co.com.powerup.user.model.user.UserModel;
import co.com.powerup.user.r2dbc.model.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
@Component
public abstract class UserModelMapper {


    public abstract UserModel entityToModel(UserEntity userEntity);

    public abstract List<UserModel> entityToModelList(List<UserEntity> userEntity);

    public abstract UserEntity modelToEntity(UserModel userModel);

    public  abstract List<UserEntity> modelToEntityList(List<UserModel> userModel);
}
