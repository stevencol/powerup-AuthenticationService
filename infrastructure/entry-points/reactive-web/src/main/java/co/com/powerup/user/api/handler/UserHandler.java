package co.com.powerup.user.api.handler;

import co.com.powerup.user.api.dto.ApiResponseDto;
import co.com.powerup.user.api.dto.UserDto;
import co.com.powerup.user.api.dto.UserPrincipal;
import co.com.powerup.user.api.helper.ValidationHelper;
import co.com.powerup.user.api.mapper.UserDtoMapper;
import co.com.powerup.user.model.user.UserModel;
import co.com.powerup.user.usecase.user.UserUseCase;
import exceptions.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static constants.MessagesInfo.*;

import static co.com.powerup.user.api.helper.ResponseHelper.responseHelper;

@Component
@Slf4j
@AllArgsConstructor
public class UserHandler {

    private final UserUseCase userUseCase;
    private final UserDtoMapper userMapper;
    private final ValidationHelper validationHelper;
    private final PasswordEncoder passwordEncoder;


    @PreAuthorize("hasAuthority('admin')")
    public Mono<ServerResponse> saveUser(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(UserDto.class)
                .flatMap(validationHelper::validFieldReactive)
                .flatMap(userDto -> {
                    UserModel userToSave = userMapper.dtoToModel(userDto);
                    userToSave.setId(null);
                    userToSave.setPassword(passwordEncoder.encode(userDto.password()));
                    return userUseCase.createUser(userToSave)
                            .flatMap(userModel -> {
                                log.info("User recibido {}", userDto.toString());
                                log.info("User guardado {}", userModel.toString());
                                UserDto userDtoResponse = userMapper.modelToDto(userModel);
                                return responseHelper(new ApiResponseDto<UserDto>(userDtoResponse, "User created successfully", HttpStatus.CREATED));
                            });

                });
    }

    public Mono<ServerResponse> getUserByEmail(ServerRequest serverRequest) {
        log.info("Starting user search by email");
        String email = serverRequest.pathVariable("email");
        return userUseCase.findUserByEmail(email)
                .flatMap(userModel -> {
                    UserDto userDtoResponse = userMapper.modelToDto(userModel);
                    return responseHelper(new ApiResponseDto<>(userDtoResponse, MSG_OPERATION_SUCCESS, HttpStatus.OK));
                });
    }

    @SuppressWarnings("unused")
    public Mono<ServerResponse> getAllUser(ServerRequest request) {


        return userUseCase.findAllUsers().map(userMapper::modelToDto)
                .collectList()
                .flatMap(userDtoList -> responseHelper(new ApiResponseDto<>(userDtoList, MSG_OPERATION_SUCCESS, HttpStatus.OK))
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
                            return userUseCase.updateUser(userModel, idUser).flatMap(
                                    userModel1 -> {
                                        UserDto userDtoResponse = userMapper.modelToDto(userModel1);
                                        return responseHelper(new ApiResponseDto<UserDto>(userDtoResponse, MSG_OPERATION_SUCCESS, HttpStatus.OK));
                                    }
                            );
                        }
                );

    }

    public Mono<ServerResponse> deleteUser(ServerRequest request) {
        log.info("Iniciando eliminacion de usuario");
        Long idUser = Long.parseLong(request.pathVariable("id"));
        return userUseCase.deleteUserById(idUser)
                .then(Mono.defer(() -> responseHelper(new ApiResponseDto<Object>(null, MSG_OPERATION_SUCCESS, HttpStatus.OK))));
    }


    @PreAuthorize("hasAnyAuthority('admin', 'client')")
    public Mono<ServerResponse> findMyUserByDocumentNumber(ServerRequest request) {

        return ReactiveSecurityContextHolder.getContext().
                flatMap(securityContext -> {
                    UserPrincipal principal = (UserPrincipal) securityContext.getAuthentication().getPrincipal();
                    return userUseCase.findUserByDocumentNumber(principal.getDocumentNumber())
                            .switchIfEmpty(Mono.error(new EntityNotFoundException("User not found")))
                            .flatMap(userModel -> {
                                        UserDto userDtoResponse = userMapper.modelToDtoNoTPass(userModel);
                                        return responseHelper(new ApiResponseDto<>(userDtoResponse, MSG_OPERATION_SUCCESS, HttpStatus.OK));
                                    }
                            );
                });

    }

    @PreAuthorize("hasAnyAuthority('admin', 'client')")
    public Mono<ServerResponse> findUserByDocumentNumber(ServerRequest request) {
        String documentNumber = request.pathVariable("documentNumber");

        return userUseCase.findUserByDocumentNumber(documentNumber)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("User not found")))
                .flatMap(userModel -> {
                            UserDto userDtoResponse = userMapper.modelToDtoNoTPass(userModel);
                            return responseHelper(new ApiResponseDto<>(userDtoResponse, MSG_OPERATION_SUCCESS, HttpStatus.OK));
                        }
                );
    }
}
