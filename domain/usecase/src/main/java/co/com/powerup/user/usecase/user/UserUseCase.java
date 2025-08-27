package co.com.powerup.user.usecase.user;

import co.com.powerup.user.model.user.UserModel;
import co.com.powerup.user.model.user.gateways.UserRepository;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@AllArgsConstructor
public class UserUseCase {


    private final UserRepository userRepository;

    public Mono<UserModel> saveUser(UserModel userModel) {
        return userRepository.saveUser(userModel);
    }

    public Mono<UserModel> getUserByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }

    public Mono<UserModel> editUser(UserModel userModel, Long id) {
        return userRepository.getUserById(id)
                .flatMap(user -> {
                    userModel.setId(user.getId());
                    return userRepository.editUser(userModel);

                });
    }

    public Flux<UserModel> getAllUser() {
        return userRepository.getAllUser();
    }

    public Mono<Void> deleteUser(Long id) {
        return userRepository.deleteUser(id);
    }
}
