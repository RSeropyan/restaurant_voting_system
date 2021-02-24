package app.service.exceptions;

import java.util.List;

public class EntityValidationException extends RuntimeException {

    private final List<String> errors;

    public EntityValidationException(List<String> errors) {
        super("Entity validation process has failed.");
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }

}
