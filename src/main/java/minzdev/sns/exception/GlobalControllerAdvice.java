package minzdev.sns.exception;

import lombok.extern.slf4j.Slf4j;
import minzdev.sns.controller.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(SnsApplicationException.class)
    public ResponseEntity<?> snsApplicationExceptionHandler(SnsApplicationException exception) {
        log.error("[Error] {}", exception.toString());
        return ResponseEntity.status(exception.getErrorCode().getStatus())
                .body(Response.error(exception.getErrorCode().name()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> runtimeExceptionHandler(RuntimeException exception) {
        log.error("[Error] {}", exception.toString());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Response.error(ErrorCode.INTERNAL_SERVER_ERROR.name()));
    }

}
