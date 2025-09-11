package co.com.powerup.user.api.helper;

import co.com.powerup.user.api.dto.UserDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Component
@AllArgsConstructor
public class ValidationHelper {


    private final Validator validator;


    public void validField(UserDto userDto) {
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(userDto, UserDto.class.getName());
        validator.validate(userDto, errors);

        if (errors.hasErrors()) {

            throw new WebExchangeBindException(null, errors);
        }
    }

    public Mono<UserDto> validFieldReactive(UserDto userDto) {
        return Mono.fromCallable(() -> {
            validField(userDto);
            return userDto;
        }).subscribeOn(Schedulers.boundedElastic());
    }
}
