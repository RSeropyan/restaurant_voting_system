package app.controller.exceptions;

import app.service.exceptions.EntityNotFoundException;
import app.service.exceptions.EntityPropertiesValidationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

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
        else if (e instanceof IllegalArgumentException) {
            return handleIllegalArgumentException(e, headers);
        }
        else if (e instanceof DataIntegrityViolationException) {
            return handleDataIntegrityViolationException(e, headers);
        }
        else if (e instanceof MethodArgumentTypeMismatchException) {
            return handleMethodArgumentTypeMismatchException(e, headers);
        }
        // This may be thrown from Service layer
        else if (e instanceof EntityPropertiesValidationException) {
            return handleEntityPropertiesValidationException((EntityPropertiesValidationException) e, headers);
        }
        // This may be thrown from Controller layer
        else if (e instanceof EntityValidationException) {
            return handleEntityValidationException((EntityValidationException) e, headers);
        }
        else {
            return handleOtherExceptions(e, headers);
        }

    }

    private ResponseEntity<ErrorMessagesList> handleEntityNotFoundException(Exception e, HttpHeaders headers) {
        return new ResponseEntity<>(new ErrorMessagesList(e), headers, HttpStatus.NOT_FOUND);
    }

    private ResponseEntity<ErrorMessagesList> handleIllegalArgumentException(Exception e, HttpHeaders headers) {
        return new ResponseEntity<>(new ErrorMessagesList(e), headers, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<ErrorMessagesList> handleDataIntegrityViolationException(Exception e, HttpHeaders headers) {
        return new ResponseEntity<>(new ErrorMessagesList("You are probably trying to save a duplicate of existing entity. For each restaurant the name must be unique. For each meal of a particular restaurant the combination of meal name, category and price must be unique."), headers, HttpStatus.CONFLICT);
    }

    private ResponseEntity<ErrorMessagesList> handleMethodArgumentTypeMismatchException(Exception e, HttpHeaders headers) {
        return new ResponseEntity<>(new ErrorMessagesList("Entity id must be a positive number."), headers, HttpStatus.BAD_REQUEST);
    }

    // This may be thrown from Service layer (part of defensive programming concept)
    private ResponseEntity<ErrorMessagesList> handleEntityPropertiesValidationException(EntityPropertiesValidationException e, HttpHeaders headers) {
        return new ResponseEntity<>(new ErrorMessagesList(e.getErrors()), headers, HttpStatus.BAD_REQUEST);
    }

    // This may be thrown from Controller layer during request body validation
    private ResponseEntity<ErrorMessagesList> handleEntityValidationException(EntityValidationException e, HttpHeaders headers) {
        return new ResponseEntity<>(new ErrorMessagesList(e.getErrors()), headers, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<ErrorMessagesList> handleOtherExceptions(Exception e, HttpHeaders headers) {
        return new ResponseEntity<>(new ErrorMessagesList(e), headers, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
