package co.com.powerup.user.api.security.service;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class LoginAttemptService {

    private final ConcurrentHashMap<String, Integer> logins = new ConcurrentHashMap<>();

    private static final  int MAX_ATTEMPTS = 5;


    public boolean isBlocked(String email) {
        return logins.getOrDefault(email, 0) >= MAX_ATTEMPTS;
    }

    public void recordAttempt(String email) {
        logins.compute(email, (key, value) -> (value == null) ? 1 : value + 1);

        Mono.delay(Duration.ofMinutes(5))
                .doOnNext(t -> logins.remove(email))
                .subscribe();
    }
}
