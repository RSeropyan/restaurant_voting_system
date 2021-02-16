package app.controller.exceptions;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class ErrorMessagesList {

    @JsonProperty("errors")
    private final List<String> errorMessages;

    public ErrorMessagesList() {
        this.errorMessages = new ArrayList<>();
    }

    public ErrorMessagesList(Exception e) {
        errorMessages = new ArrayList<>();
        errorMessages.add(e.getMessage());
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
}
