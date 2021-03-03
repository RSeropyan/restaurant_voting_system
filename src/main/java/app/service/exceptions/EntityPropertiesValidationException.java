package app.service.exceptions;

import java.util.List;

public class EntityPropertiesValidationException extends RuntimeException {

    private final List<String> errors;

    public EntityPropertiesValidationException(String message, List<String> errors) {
        super(message);
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }

}