package co.com.powerup.user.usecase.user;

import co.com.powerup.user.model.user.UserModel;
import co.com.powerup.user.model.user.gateways.RoleRepository;
import co.com.powerup.user.model.user.gateways.UserRepository;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;


@AllArgsConstructor
public class UserUseCase {


    private final UserRepository userRepository;
    private final RoleUseCase roleUseCase;


    public Mono<UserModel> createUser(UserModel userModel) {

        return roleUseCase.findRoleByName("client")
                .flatMap(role -> {
                    userModel.setRolId(role.getId());
                    return userRepository.createUser(userModel);
                });

    }

    public Mono<UserModel> findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    public Mono<UserModel> updateUser(UserModel userModel, Long id) {
        return userRepository.findUserById(id)
                .flatMap(user -> {
                    userModel.setId(user.getId());
                    return userRepository.updateUser(userModel);

                });
    }


    public Mono<List<UserModel>> findUserAdmins() {
        return roleUseCase.findRoleByName("admin")
                .flatMapMany(role -> userRepository.findByRolId(role.getId()))
                .collectList();
    }

    public Flux<UserModel> findAllUsers() {
        return userRepository.findAllUsers();
    }

    public Mono<Void> deleteUserById(Long id) {
        return userRepository.deleteUserById(id);
    }

    public Mono<UserModel> findUserByDocumentNumber(String documentNumber) {

        return userRepository.findByDocumentNumber(documentNumber);
    }


}
