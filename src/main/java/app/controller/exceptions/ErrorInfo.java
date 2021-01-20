package app.controller.exceptions;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "errorClass", "message" })
public class ErrorInfo {

    private final String errorClass;
    private final String errorMessage;

    public ErrorInfo(Exception e) {
        this.errorClass = e.getClass().getSimpleName();
        this.errorMessage = e.getMessage();
    }

    public String getErrorClass() {
        return errorClass;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

}
