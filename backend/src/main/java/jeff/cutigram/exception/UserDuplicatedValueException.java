package jeff.cutigram.exception;

public class UserDuplicatedValueException extends RuntimeException {
    public UserDuplicatedValueException(String column) {
        super(column);
    }
}
