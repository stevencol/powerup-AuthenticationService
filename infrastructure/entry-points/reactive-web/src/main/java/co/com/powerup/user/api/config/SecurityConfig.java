package co.com.powerup.user.api.config;

import co.com.powerup.user.api.security.adapter.UserDetailsAdapter;
import co.com.powerup.user.api.security.exception.CustomAccessDeniedHandler;
import co.com.powerup.user.api.security.exception.CustomAuthenticationEntry;
import co.com.powerup.user.api.security.filter.AuthorizationFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@Configuration
@AllArgsConstructor
public class SecurityConfig {

    private final AuthorizationFilter authorizationFilter;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final CustomAuthenticationEntry authenticationEntry;

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(cors -> {
                })
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/login",
                                "/swagger",
                                "/swagger/**",
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/docs",
                                "/docs/**",
                                "/v3/api-docs",
                                "/v3/api-docs/**").permitAll()
                        .anyExchange().authenticated()
                ).addFilterBefore(authorizationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .exceptionHandling(exceptions ->
                        exceptions.authenticationEntryPoint(authenticationEntry)
                                .accessDeniedHandler(accessDeniedHandler)
                )
                .build();

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new Argon2PasswordEncoder(16, 23, 1, 4096, 3);
    }


    @Bean
    public ReactiveAuthenticationManager authenticationProvider(UserDetailsAdapter userDetailsAdapter, PasswordEncoder passwordEncoder) {
        UserDetailsRepositoryReactiveAuthenticationManager authManager =
                new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsAdapter);
        authManager.setPasswordEncoder(passwordEncoder);
        return authManager;
    }
}
