package app.controller.exceptions;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ErrorMessagesList {

    @JsonProperty("errors")
    private final List<String> errorMessages;

    public ErrorMessagesList() {
        this.errorMessages = new ArrayList<>();
    }

    public ErrorMessagesList(Exception e) {
        errorMessages = new ArrayList<>();
        Throwable rootCause = findRootCause(e);
        errorMessages.add(rootCause.getMessage());
    }

    public ErrorMessagesList(String errorMessage) {
        errorMessages = new ArrayList<>();
        errorMessages.add(errorMessage);
    }

    public ErrorMessagesList(List<String> errorMessages) {
        this.errorMessages = errorMessages;
    }

    public List<String> getErrorMessages() {
        return errorMessages;
    }

    private static Throwable findRootCause(Exception e) {
        Objects.requireNonNull(e);
        Throwable rootCause = e;
        while (rootCause.getCause() != null && rootCause.getCause() != rootCause) {
            rootCause = rootCause.getCause();
        }
        return rootCause;
    }

}
