package app.controller.exceptions;

import java.util.List;

public class EntityValidationException extends RuntimeException {

    private final List<String> errors;

    public EntityValidationException(List<String> errors) {
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }

}