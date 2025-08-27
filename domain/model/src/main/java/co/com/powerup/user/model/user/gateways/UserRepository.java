package co.com.powerup.user.model.user.gateways;

import co.com.powerup.user.model.user.UserModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepository {

    Mono<UserModel> saveUser(UserModel userModel);

    Mono<UserModel> getUserByEmail(String email);

    Mono<UserModel> getUserById(Long id);

    Mono<UserModel> editUser(UserModel userModel);

    Flux<UserModel> getAllUser();

    Mono<Void> deleteUser(Long id);
}
