package io.moneytransfer.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ApiResponse {

    @JsonProperty("type")
    private String type;

    @JsonProperty("message")
    private String message;

    public ApiResponse() {
    }

    public ApiResponse(String type, String message) {
        this.message = message;
        this.type = type;
    }

    public String getMessage() {
        return message;
    }
}
