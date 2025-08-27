package co.com.powerup.user.r2dbc;

import co.com.powerup.user.model.user.UserModel;
import co.com.powerup.user.model.user.exception.EntityNotFoundException;
import co.com.powerup.user.r2dbc.mapper.UserModelMapper;
import co.com.powerup.user.r2dbc.model.UserEntity;
import co.com.powerup.user.r2dbc.repository.R2BDUserRepositoryAdapter;
import co.com.powerup.user.r2dbc.repository.R2DBUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import co.com.powerup.user.r2dbc.mapper.UserModelMapperImpl;

@ExtendWith(MockitoExtension.class)
class MyReactiveRepositoryAdapterTest {
    private R2DBUserRepository userRepository;
    private UserModelMapper userMapper;
    private R2BDUserRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(R2DBUserRepository.class);
        userMapper = new UserModelMapperImpl();
        adapter = new R2BDUserRepositoryAdapter(userRepository, userMapper);
    }

    @Test
    void saveUser() {
        UserModel model = buildModel();
        UserEntity entity = userMapper.modelToEntity(model);

        when(userRepository.save(any(UserEntity.class)))
                .thenReturn(Mono.just(entity));

        StepVerifier.create(adapter.saveUser(model))
                .expectNextMatches(saved -> saved.getEmail().equals("steven@email.com"))
                .verifyComplete();

        verify(userRepository).save(any(UserEntity.class));
    }

    @Test
    void editUser() {

        UserModel update = buildModel();
        update.setId(1L);
        update.setMiddleName("Carlos");
        update.setLastName("Rodriguez");
        update.setSecondLastName("NuevoSecond");
        update.setEmail("mail@email.com");
        update.setBaseSalary(new BigDecimal("2000.0"));
        update.setAddress("Avenida Siempre Viva 123");
        update.setPhoneNumber("222");
        update.setBirthDate(LocalDate.of(1999, 1, 1));


        when(userRepository.save(any(UserEntity.class)))
                .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));


        StepVerifier.create(adapter.editUser(update))
                .assertNext(updated -> {
                    Objects.requireNonNull(updated);
                    assert updated.getId().equals(1L);
                    assert updated.getMiddleName().equals("Carlos");
                    assert updated.getLastName().equals("Rodriguez");
                    assert updated.getSecondLastName().equals("NuevoSecond");
                    assert updated.getEmail().equals("mail@email.com");
                    assert updated.getBaseSalary().equals(new BigDecimal("2000.0"));
                    assert updated.getAddress().equals("Avenida Siempre Viva 123");
                    assert updated.getPhoneNumber().equals("222");
                    assert updated.getBirthDate().equals(LocalDate.of(1999, 1, 1));
                })
                .verifyComplete();

        verify(userRepository).save(any(UserEntity.class));
    }


    @Test
    void getUserByEmail() {

        UserEntity userEntity = userMapper.modelToEntity(buildModel());

        when(userRepository.findByEmail("steven@email.com"))
                .thenReturn(Mono.just(userEntity));

        StepVerifier.create(adapter.getUserByEmail("steven@email.com"))
                .expectNextMatches(user -> user.getEmail().equals("steven@email.com"))
                .verifyComplete();
    }

    @Test
    void getUserById() {
        UserEntity userEntity = userMapper.modelToEntity(buildModel());
        when(userRepository.findById(1L))
                .thenReturn(Mono.just(userEntity));
        StepVerifier.create(adapter.getUserById(1L))
                .expectNextMatches(userModel -> userModel.getId().equals(1L))
                .verifyComplete();
    }

    @Test
    void getUserByIdNotFound() {

        when(userRepository.findById(1L))
                .thenReturn(Mono.empty());
        StepVerifier.create(adapter.getUserById(1L))
                .expectErrorMatches(EntityNotFoundException.class::isInstance)
                .verify();
    }


    @Test
    void getUserByEmailNotFound() {

        when(userRepository.findByEmail("steven@email.com"))
                .thenReturn(Mono.empty());

        StepVerifier.create(adapter.getUserByEmail("steven@email.com"))
                .expectErrorMatches(EntityNotFoundException.class::isInstance)
                .verify();
    }


    @Test
    void getAllUser() {
        UserEntity entity = userMapper.modelToEntity(buildModel());
        when(userRepository.findAll()).thenReturn(Flux.just(entity));

        StepVerifier.create(adapter.getAllUser())
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void deleteUser() {
        UserEntity entity = userMapper.modelToEntity(buildModel());

        when(userRepository.findById(1L)).thenReturn(Mono.just(entity));
        when(userRepository.deleteById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(adapter.deleteUser(1L))
                .verifyComplete();

        verify(userRepository).deleteById(1L);
    }

    @Test
    void deleteUserNotFound() {
        when(userRepository.findById(99L)).thenReturn(Mono.empty());

        StepVerifier.create(adapter.deleteUser(99L))
                .expectErrorMatches(EntityNotFoundException.class::isInstance)
                .verify();
    }

    private UserModel buildModel() {
        UserModel user = new UserModel();
        user.setId(1L);
        user.setFirstName("Steven");
        user.setLastName("Palacio");
        user.setEmail("steven@email.com");
        user.setBaseSalary(BigDecimal.valueOf(1000.0));
        user.setAddress("Calle falsa 123");
        user.setBirthDate(LocalDate.of(1994, 5, 24));
        user.setPhoneNumber("3016130000");
        return user;
    }
}