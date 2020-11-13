package com.steven.willysbakeshop.util.exceptions;

import java.time.ZonedDateTime;

public class ErrorDetails {
    private final ZonedDateTime timestamp;
    private final String message;
    private final String details;

    public ErrorDetails(String message, String details) {
        super();
        this.timestamp = ZonedDateTime.now();
        this.message = message;
        this.details = details;
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getDetails() {
        return details;
    }
}
