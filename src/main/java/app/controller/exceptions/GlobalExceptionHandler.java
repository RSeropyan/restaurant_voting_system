package app.controller.exceptions;

import app.service.exceptions.EntityNotFoundException;
//import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorMessagesList> handleControllerExceptions(Exception e) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=UTF-8");
        headers.add("Cache-Control", "no-store");

        if (e instanceof EntityNotFoundException) {
            return handleEntityNotFoundException(e, headers);
        }
        else {
            return handleOtherExceptions(e, headers);
        }

    }

    private ResponseEntity<ErrorMessagesList> handleEntityNotFoundException(Exception e, HttpHeaders headers) {
        return new ResponseEntity<>(new ErrorMessagesList(e), headers, HttpStatus.NOT_FOUND);
    }

    private ResponseEntity<ErrorMessagesList> handleOtherExceptions(Exception e, HttpHeaders headers) {
        return new ResponseEntity<>(new ErrorMessagesList(e), headers, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
