package app.controller.exceptions;

import app.service.exceptions.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorInfo> handleControllerExceptions(Exception e) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=UTF-8");
        headers.add("Cache-Control", "no-store");

        if (e instanceof EntityNotFoundException) {
            return handleEntityNotFoundException(e, headers);
        }
        else if (e instanceof MethodArgumentTypeMismatchException) {
            return handleMethodArgumentTypeMismatchException(e, headers);
        }
        else if (e instanceof DataIntegrityViolationException) {
            return handleMethodDataIntegrityViolation(e, headers);
        }
        else if (e instanceof HttpMessageNotReadableException) {
            return handleMethodHttpMessageNotReadableException(e, headers);
        }
        else {
            return handleOtherExceptions(e, headers);
        }

    }

    private ResponseEntity<ErrorInfo> handleEntityNotFoundException(Exception e, HttpHeaders headers) {
        return new ResponseEntity<>(new ErrorInfo(e), headers, HttpStatus.NOT_FOUND);
    }

    // When the request parameter of URL such as 'id' has an inappropriate type
    private ResponseEntity<ErrorInfo> handleMethodArgumentTypeMismatchException(Exception e, HttpHeaders headers) {
        return new ResponseEntity<>(new ErrorInfo(e), headers, HttpStatus.BAD_REQUEST);
    }

    // When the duplicate of existing entity (restaurant or meal of particular restaurant) is being saved
    private ResponseEntity<ErrorInfo> handleMethodDataIntegrityViolation(Exception e, HttpHeaders headers) {
        return new ResponseEntity<>(new ErrorInfo(e), headers, HttpStatus.BAD_REQUEST);
    }

    // When the property of request body has an inappropriate type
    private ResponseEntity<ErrorInfo> handleMethodHttpMessageNotReadableException(Exception e, HttpHeaders headers) {
        return new ResponseEntity<>(new ErrorInfo(e), headers, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<ErrorInfo> handleOtherExceptions(Exception e, HttpHeaders headers) {
        return new ResponseEntity<>(new ErrorInfo(e), headers, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
