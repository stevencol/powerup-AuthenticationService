package co.com.powerup.user.usecase.user;

import co.com.powerup.user.model.user.UserModel;
import co.com.powerup.user.model.user.gateways.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;


class UserCaseTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserUseCase userUseCase;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createUser_Success() {

        UserModel userModel = getUserModelTest();

        when(userRepository.createUser(any(UserModel.class))).thenReturn(Mono.just(userModel));

        StepVerifier.create(userUseCase.createUser(userModel))
                .expectNext(userModel)
                .verifyComplete();

        verify(userRepository).createUser(any(UserModel.class));
    }


    private static UserModel getUserModelTest() {
        UserModel userModel = new UserModel();
        userModel.setFirstName("Steven");
        userModel.setMiddleName("Andres");
        userModel.setLastName("Gomez");
        userModel.setSecondLastName("Lopez");
        userModel.setEmail("steven@mail.com");
        userModel.setAddress("Cra 7 # 45-67");
        userModel.setPhoneNumber("3216549870");
        userModel.setBaseSalary(new BigDecimal("1500.00"));
        return userModel;

    }


    @Test
    void find_By_Email() {


        UserModel userModel = getUserModelTest();
        when(userRepository.findUserByEmail(any(String.class)))
                .thenReturn(Mono.just(userModel));

        StepVerifier
                .create(userUseCase.findUserByEmail("steven@mail.com"))
                .expectNextMatches(user -> user.getEmail().equals("steven@mail.com"))
                .verifyComplete();

        verify(userRepository).findUserByEmail(any(String.class));
    }

    @Test
    void find_By_Email_Not_Found() {


        when(userRepository.findUserByEmail(any(String.class)))
                .thenReturn(Mono.empty());

        StepVerifier
                .create(userUseCase.findUserByEmail("steven@mail.com"))
                .verifyComplete();

        verify(userRepository).findUserByEmail(any(String.class));
    }

    @Test
    void find_All_Users() {

        UserModel userModel = getUserModelTest();
        UserModel userModel2 = getUserModelTest();

        when(userRepository.findAllUsers())
                .thenReturn(Flux.just(userModel, userModel2));

        StepVerifier
                .create(userUseCase.findAllUsers())
                .expectNext(userModel)
                .expectNext(userModel2)
                .verifyComplete();

        verify(userRepository).findAllUsers();
    }

    @Test
    void delete_User_By_Id() {
        Long userId = 1L;

        when(userRepository.deleteUserById(userId)).thenReturn(Mono.empty());


        StepVerifier.create(userUseCase.deleteUserById(userId))
                .verifyComplete();

        verify(userRepository).deleteUserById(userId);
    }


    @Test
    void update_User_Success() {
        Long userId = 1L;


        UserModel existingUser = getUserModelTest();
        existingUser.setId(userId);

        UserModel updatedData = getUserModelTest();
        updatedData.setFirstName("Carlos");

        UserModel savedUser = getUserModelTest();
        savedUser.setId(userId);
        savedUser.setFirstName("Carlos");


        when(userRepository.findUserById(userId))
                .thenReturn(Mono.just(existingUser));


        when(userRepository.updateUser(any(UserModel.class)))
                .thenReturn(Mono.just(savedUser));

        StepVerifier.create(userUseCase.updateUser(updatedData, userId))
                .expectNext(savedUser)
                .verifyComplete();

        verify(userRepository).findUserById(userId);
        verify(userRepository).updateUser(argThat(u ->
                u.getId().equals(userId) && u.getFirstName().equals("Carlos")
        ));
    }

    @Test
    void updateUser_UserNotFound() {
        Long userId = 1L;
        UserModel updatedData = getUserModelTest();
        updatedData.setFirstName("Carlos");

        when(userRepository.findUserById(userId)).thenReturn(Mono.empty());

        StepVerifier.create(userUseCase.updateUser(updatedData, userId))
                .verifyComplete();

        verify(userRepository).findUserById(userId);
    }

    @Test
    void createUser_Error() {
        UserModel userModel = getUserModelTest();

        when(userRepository.createUser(any(UserModel.class)))
                .thenReturn(Mono.error(new RuntimeException("Error en la creación del usuario")));

        StepVerifier.create(userUseCase.createUser(userModel))
                .expectError(RuntimeException.class)
                .verify();

        verify(userRepository).createUser(any(UserModel.class));
    }

    @Test
    void findUserByDocumentNumber_Success() {
        String documentNumber = "123456789";
        UserModel userModel = getUserModelTest();
        when(userRepository.findByDocumentNumber(documentNumber))
                .thenReturn(Mono.just(userModel));

        StepVerifier.create(userUseCase.findUserByDocumentNumber(documentNumber))
                .expectNext(userModel)
                .verifyComplete();

        verify(userRepository).findByDocumentNumber(documentNumber);
    }

    @Test
    void findUserByDocumentNumber_NotFound() {
        String documentNumber = "123456789";

        when(userRepository.findByDocumentNumber(documentNumber))
                .thenReturn(Mono.empty());

        StepVerifier.create(userUseCase.findUserByDocumentNumber(documentNumber))
                .verifyComplete();
        verify(userRepository).findByDocumentNumber(documentNumber);
    }
}
