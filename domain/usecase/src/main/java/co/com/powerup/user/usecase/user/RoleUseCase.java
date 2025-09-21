package co.com.powerup.user.usecase.user;

import co.com.powerup.user.model.user.gateways.RoleRepository;
import co.com.powerup.user.model.user.model.RoleModel;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@AllArgsConstructor

public class RoleUseCase {

    private final RoleRepository roleRepository;


    public Mono<RoleModel> findRoleById(Long id) {
        return roleRepository.findRoleById(id);
    }

    public Mono<RoleModel> findRoleByName(String name) {
        return roleRepository.findRoleByName(name);
    }
}
