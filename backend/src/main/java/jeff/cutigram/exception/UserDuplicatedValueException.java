package jeff.cutigram.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UserDuplicatedValueException extends RuntimeException {
    public UserDuplicatedValueException(String column) {
        super(column);
    }
}
