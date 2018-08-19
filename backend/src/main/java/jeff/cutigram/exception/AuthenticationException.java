package jeff.cutigram.exception;

import org.omg.SendingContext.RunTime;
import org.springframework.security.core.Authentication;

public class AuthenticationException extends RuntimeException {
    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthenticationException(String message) {
        super(message);
    }
}
