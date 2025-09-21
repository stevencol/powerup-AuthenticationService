package co.com.powerup.user.r2dbc.repository;


import co.com.powerup.user.model.user.UserModel;
import co.com.powerup.user.model.user.gateways.RoleRepository;
import co.com.powerup.user.model.user.model.RoleModel;
import co.com.powerup.user.r2dbc.mapper.RoleMapper;
import exceptions.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import static constants.MessageExceptions.*;

@Repository
@AllArgsConstructor
public class R2BDRolRepositoryAdapter implements RoleRepository {


    private final R2DBRolRepository rolRepository;
    private final RoleMapper roleMapper;


    @Override
    public Mono<RoleModel> createRole(RoleModel userModel) {
        return null;
    }

    @Override
    public Mono<RoleModel> findRoleById(Long id) {
        return rolRepository.findById(id)
                .switchIfEmpty(Mono.error(new EntityNotFoundException(MSG_NOT_FOUND)))
                .map(roleMapper::toModel);
    }

    @Override
    public Mono<RoleModel> findRoleByName(String name) {
        return rolRepository.findByName(name)
                .switchIfEmpty(Mono.error(new EntityNotFoundException(MSG_NOT_FOUND)))
                .map(roleMapper::toModel);
    }

    @Override
    public Mono<RoleModel> updateRole(UserModel userModel) {
        return null;
    }

    @Override
    public Mono<Void> deleteRoleById(Long id) {
        return null;
    }
}
