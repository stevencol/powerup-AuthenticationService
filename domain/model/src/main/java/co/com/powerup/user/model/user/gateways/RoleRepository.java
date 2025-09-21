package co.com.powerup.user.model.user.gateways;

import co.com.powerup.user.model.user.UserModel;
import co.com.powerup.user.model.user.model.RoleModel;
import reactor.core.publisher.Mono;

public interface RoleRepository {

    Mono<RoleModel> createRole(RoleModel userModel);

    Mono<RoleModel> findRoleById(Long id);

    Mono<RoleModel> findRoleByName(String name);

    Mono<RoleModel> updateRole(UserModel userModel);

    Mono<Void> deleteRoleById(Long id);

}
