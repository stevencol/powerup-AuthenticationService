package co.com.powerup.user.r2dbc.repository;



import co.com.powerup.user.r2dbc.model.RolEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;


@Repository
public interface R2DBRolRepository extends ReactiveCrudRepository<RolEntity, Long> {

    Mono<RolEntity> findByName(String name);


}
