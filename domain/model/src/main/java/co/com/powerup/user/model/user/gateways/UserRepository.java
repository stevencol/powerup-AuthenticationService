package co.com.powerup.user.model.user.gateways;

import co.com.powerup.user.model.user.UserModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepository {

    Mono<UserModel> createUser(UserModel userModel);

    Mono<UserModel> findUserByEmail(String email);

    Mono<UserModel> findUserById(Long id);

    Mono<UserModel> updateUser(UserModel userModel);

    Flux<UserModel> findAllUsers();
    Flux<UserModel> findByRolId(Long rolId);

    Mono<Void> deleteUserById(Long id);

    Mono<UserModel> findByDocumentNumber(String email);
}
