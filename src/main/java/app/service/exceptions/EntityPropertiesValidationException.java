package app.service.exceptions;

import java.util.List;

public class EntityPropertiesValidationException extends RuntimeException {

    private final List<String> errors;

    public EntityPropertiesValidationException(List<String> errors) {
        super("Failed to validate entity properties.");
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }

}