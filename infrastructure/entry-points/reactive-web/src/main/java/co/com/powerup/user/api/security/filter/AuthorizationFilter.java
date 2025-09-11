package co.com.powerup.user.api.security.filter;

import co.com.powerup.user.api.security.service.AuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@AllArgsConstructor
public class AuthorizationFilter implements WebFilter {

    private final AuthenticationService authenticationService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        log.info("Starting AuthFilter");
        String token = exchange.getRequest().getHeaders().getFirst("Authorization");

        if (token == null || !token.startsWith("Bearer ")) {
            log.warn("No token provided or invalid format");
            return chain.filter(exchange);

        }
        log.info("valid token");

        return authenticationService.getAuthentication(token)
                .flatMap(authentication -> {
                    log.info("Authentication successful for: " + authentication.getName());
                    return chain.filter(exchange)
                            .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
                });

    }



}

