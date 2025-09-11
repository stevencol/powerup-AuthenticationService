package co.com.powerup.user.api.security.adapter;

import co.com.powerup.user.usecase.user.RoleUseCase;
import co.com.powerup.user.usecase.user.UserUseCase;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static constants.MessageExceptions.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class UserDetailsAdapter implements ReactiveUserDetailsService {
    private final UserUseCase userUseCase;
    private final RoleUseCase roleUseCase;


    @Override
    public Mono<UserDetails> findByUsername(String username) throws UsernameNotFoundException {
        return userUseCase.findUserByEmail(username).onErrorResume(ex -> Mono.error(new RuntimeException(MSG_CREDENTIALS_INVALID + ex.getMessage())))
                .flatMap(user -> roleUseCase.findRoleById(user.getRolId()).map(role -> {
                    List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
                    grantedAuthorities.add((new SimpleGrantedAuthority(role.getName())));
                    return new User(user.getEmail(), user.getPassword(), true, true, true, true, grantedAuthorities);

                }));


    }


}
