package co.com.powerup.user.r2dbc.repository;


import co.com.powerup.user.model.user.UserModel;
import co.com.powerup.user.model.user.gateways.UserRepository;
import co.com.powerup.user.r2dbc.mapper.UserModelMapper;
import exceptions.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static constants.MessageExceptions.*;

@Slf4j
@Repository
@AllArgsConstructor
public class R2BDUserRepositoryAdapter implements UserRepository {


    private final R2DBUserRepository userRepository;
    private final UserModelMapper userMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Mono<UserModel> createUser(UserModel userModel) {
        return userRepository.save(userMapper.modelToEntity(userModel))
                .map(userMapper::entityToModel);
    }


    @Override
    @Transactional(readOnly = true)
    public Mono<UserModel> findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .switchIfEmpty(Mono.error(new EntityNotFoundException(String.format(MSG_NOT_FOUND, email))))
                .map(userMapper::entityToModel);
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<UserModel> findUserById(Long id) {
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(new EntityNotFoundException(String.format(MSG_NOT_FOUND, id))))
                .map(userMapper::entityToModel);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Mono<UserModel> updateUser(UserModel userModel) {

        return userRepository.save(userMapper.modelToEntity(userModel))
                .map(userMapper::entityToModel);
    }

    @Transactional(readOnly = true)
    @Override
    public Flux<UserModel> findAllUsers() {
        return userRepository.findAll()
                .map(userMapper::entityToModel);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Mono<Void> deleteUserById(Long id) {
        return userRepository.findById(id).
                switchIfEmpty(Mono.error(new EntityNotFoundException(String.format(MSG_NOT_FOUND, id))))
                .flatMap(user -> userRepository.deleteById(id));

    }

    @Override
    public Mono<UserModel> findByDocumentNumber(String documentNumber) {
        return userRepository.findByDocumentNumber(documentNumber)
                .switchIfEmpty(Mono.error(new EntityNotFoundException(String.format(MSG_NOT_FOUND, documentNumber))))
                .map(userEntity -> {
                    log.info(userEntity.getDocumentNumber());
                    return userMapper.entityToModel(userEntity);
                });
    }


}
