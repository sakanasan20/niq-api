package tw.niq.api.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;
import tw.niq.api.model.ErrorModel;

@Slf4j
@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler({ LockedException.class })
    @ResponseBody
    public ResponseEntity<ErrorModel> handleLockedException(LockedException ex) {
    	log.debug("CustomExceptionHandler - handleLockedException");
    	HttpStatusCode status = HttpStatus.LOCKED;
    	ErrorModel errorModel = new ErrorModel(LocalDateTime.now(), ex.getMessage());
        return ResponseEntity.status(status).body(errorModel);
    }
		
    @ExceptionHandler({ AuthenticationException.class })
    @ResponseBody
    public ResponseEntity<ErrorModel> handleAuthenticationException(AuthenticationException ex) {
    	log.debug("CustomExceptionHandler - handleAuthenticationException");
    	HttpStatusCode status = HttpStatus.UNAUTHORIZED;
    	ErrorModel errorModel = new ErrorModel(LocalDateTime.now(), ex.getMessage());
        return ResponseEntity.status(status).body(errorModel);
    }
	
}
