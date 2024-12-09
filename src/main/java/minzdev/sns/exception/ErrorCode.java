package minzdev.sns.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 401 - UNAUTHORIZED
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "INVALID_PASSWORD"),

    // 404 - NOT_FOUND
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_NOT_FOUND"),

    // 409 - CONFLICT
    DUPLICATED_USER_NAME(HttpStatus.CONFLICT, "DUPLICATED_USER_NAME"),

    // 500 - INTERNAL_SERVER_ERROR
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR")
    ;

    private final HttpStatus status;
    private final String message;

}
