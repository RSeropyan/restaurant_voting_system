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
        this.errorMessages = new ArrayList<>();
        this.addErrorMessage(e);
    }

    public void addErrorMessage(String message) {
        errorMessages.add(message);
    }

    public void addErrorMessage(Exception e) {
        errorMessages.add(e.getMessage());
    }

    public List<String> getErrorMessages() {
        return errorMessages;
    }
}
