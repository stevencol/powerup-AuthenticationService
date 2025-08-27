package co.com.powerup.user.r2dbc.repository;


import co.com.powerup.user.model.user.UserModel;
import co.com.powerup.user.model.user.gateways.UserRepository;
import co.com.powerup.user.model.user.exception.EntityNotFoundException;
import co.com.powerup.user.r2dbc.mapper.UserModelMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@AllArgsConstructor
public class R2BDUserRepositoryAdapter implements UserRepository {


    private final R2DBUserRepository userRepository;
    private final UserModelMapper userMapper;

    @Override
    public Mono<UserModel> saveUser(UserModel userModel) {
        return userRepository.save(userMapper.modelToEntity(userModel))
                .map(userMapper::entityToModel);
    }


    @Override
    public Mono<UserModel> getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("User not found with email: " + email)))
                .map(userMapper::entityToModel);
    }

    @Override
    public Mono<UserModel> getUserById(Long id) {
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("User not found with id: " + id)))
                .map(userMapper::entityToModel);
    }

    @Override
    public Mono<UserModel> editUser(UserModel userModel) {

        return userRepository.save(userMapper.modelToEntity(userModel))
                .map(userMapper::entityToModel);
    }


    @Override
    public Flux<UserModel> getAllUser() {
        return userRepository.findAll()
                .map(userMapper::entityToModel);
    }

    @Override
    public Mono<Void> deleteUser(Long id) {
        return userRepository.findById(id).
                switchIfEmpty(Mono.error(new EntityNotFoundException("User not found with id: " + id)))
                .flatMap(user -> userRepository.deleteById(id));

    }

}
