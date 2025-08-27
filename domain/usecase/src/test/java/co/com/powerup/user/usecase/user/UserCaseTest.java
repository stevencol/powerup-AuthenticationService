package co.com.powerup.user.usecase.user;

import co.com.powerup.user.model.user.UserModel;
import co.com.powerup.user.model.user.gateways.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;

import java.math.BigDecimal;
import java.time.LocalDate;

class UserCaseTest {

    private UserRepository userRepository;
    private UserUseCase userUseCase;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        userUseCase = new UserUseCase(userRepository);
    }

    @Test
    void saveUser() {
        UserModel user = createUser();

        when(userRepository.saveUser(any(UserModel.class))).thenReturn(Mono.just(user));

        StepVerifier.create(userUseCase.saveUser(user))
                .expectNext(user)
                .verifyComplete();

        verify(userRepository).saveUser(user);
    }

    @Test
    void getUserByEmail() {
        UserModel user = createUser();

        when(userRepository.getUserByEmail("test@email.com"))
                .thenReturn(Mono.just(user));

        StepVerifier.create(userUseCase.getUserByEmail("test@email.com"))
                .expectNext(user)
                .verifyComplete();

        verify(userRepository).getUserByEmail("test@email.com");
    }

    @Test
    void editUser() {
        UserModel existing = createUser();
        existing.setFirstName("Calos");

        UserModel update = createUser();
        update.setFirstName("Michael");

        when(userRepository.getUserById(1L)).thenReturn(Mono.just(existing));
        when(userRepository.editUser(any(UserModel.class)))
                .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        StepVerifier.create(userUseCase.editUser(update, 1L))
                .assertNext(updated -> {
                    assert updated.getFirstName().equals("Michael");
                })
                .verifyComplete();

        verify(userRepository).getUserById(1L);
        verify(userRepository).editUser(any(UserModel.class));
    }


    @Test
    void getAllUser() {
        when(userRepository.getAllUser())
                .thenReturn(Flux.just(createUser(), createUser()));

        StepVerifier.create(userUseCase.getAllUser())
                .expectNextCount(2)
                .verifyComplete();

        verify(userRepository).getAllUser();
    }

    @Test
    void deleteUser() {
        when(userRepository.deleteUser(1L)).thenReturn(Mono.empty());

        StepVerifier.create(userUseCase.deleteUser(1L))
                .verifyComplete();

        verify(userRepository).deleteUser(1L);
    }

    private UserModel createUser() {
        UserModel user = new UserModel();
        user.setId(1L);
        user.setFirstName("Steven");
        user.setLastName("Palacio");
        user.setEmail("steven@email.com");
        user.setBaseSalary(new BigDecimal("1000.0"));
        user.setAddress("Calle falsa 123");
        user.setBirthDate(LocalDate.of(1994, 5, 24));
        user.setPhoneNumber("3016130000");
        return user;
    }

}
