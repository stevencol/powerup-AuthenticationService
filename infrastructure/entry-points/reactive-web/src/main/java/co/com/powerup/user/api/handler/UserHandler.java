package co.com.powerup.user.api.handler;

import co.com.powerup.user.api.dto.ApiResponseDto;
import co.com.powerup.user.api.dto.UserDto;
import co.com.powerup.user.api.helper.ValidationHelper;
import co.com.powerup.user.api.mapper.UserDtoMapper;
import co.com.powerup.user.model.user.UserModel;
import co.com.powerup.user.usecase.user.UserUseCase;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static co.com.powerup.user.api.helper.ResponseHelper.responseHelper;

@Component
@Slf4j
@AllArgsConstructor
public class UserHandler {

    private final UserUseCase userUseCase;
    private final UserDtoMapper userMapper;
    private final ValidationHelper validationHelper;

    public Mono<ServerResponse> saveUser(ServerRequest serverRequest) {

        return serverRequest.bodyToMono(UserDto.class)
                .flatMap(validationHelper::validFieldReactive)
                .flatMap(userDto -> {
                    UserModel userToSave = userMapper.dtoToModel(userDto);
                    userToSave.setId(null);
                    return userUseCase.saveUser(userToSave)

                            .flatMap(userModel -> {
                                log.info("User recibido {}", userDto.toString());
                                log.info("User guardado {}", userModel.toString());
                                UserDto userDtoResponse = userMapper.modelToDto(userModel);

                                return responseHelper(new ApiResponseDto<UserDto>(userDtoResponse, "User created successfully", HttpStatus.CREATED));
                            });

                });
    }

    public Mono<ServerResponse> getUserEmail(ServerRequest serverRequest) {
        String email = serverRequest.pathVariable("email");
        return userUseCase.getUserByEmail(email)
                .flatMap(userModel -> {
                    UserDto userDtoResponse = userMapper.modelToDto(userModel);
                    return responseHelper(new ApiResponseDto<>(userDtoResponse, "User get successfully", HttpStatus.OK));
                });
    }

    @SuppressWarnings("unused")
    public Mono<ServerResponse> getAllUser(ServerRequest request) {


        return userUseCase.getAllUser().map(userMapper::modelToDto)
                .collectList()
                .flatMap(userDtoList -> responseHelper(new ApiResponseDto<>(userDtoList, "Users retrieved successfully", HttpStatus.OK))
                );

    }

    public Mono<ServerResponse> editUser(ServerRequest request) {
        log.info("Iniciando edicion de usuario");
        Long idUser = Long.parseLong(request.pathVariable("id"));
        return request.bodyToMono(UserDto.class)
                .flatMap(validationHelper::validFieldReactive)
                .flatMap(

                        userDto -> {

                            UserModel userModel = userMapper.dtoToModel(userDto);
                            return userUseCase.editUser(userModel, idUser).flatMap(
                                    userModel1 -> {
                                        UserDto userDtoResponse = userMapper.modelToDto(userModel1);
                                        return responseHelper(new ApiResponseDto<UserDto>(userDtoResponse, "User edited successfully", HttpStatus.OK));
                                    }
                            );
                        }
                );

    }

    public Mono<ServerResponse> deleteUser(ServerRequest request) {
        log.info("Iniciando eliminacion de usuario");
        Long idUser = Long.parseLong(request.pathVariable("id"));
        return userUseCase.deleteUser(idUser)
                .then(Mono.defer(() -> responseHelper(new ApiResponseDto<Object>(null, "User deleted successfully", HttpStatus.OK))));
    }


}
