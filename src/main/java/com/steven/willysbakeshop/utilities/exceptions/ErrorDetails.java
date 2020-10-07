package com.steven.willysbakeshop.utilities.exceptions;

import java.time.ZonedDateTime;

public class ErrorDetails {
    private ZonedDateTime timestamp;
    private String message;
    private String details;

    public ErrorDetails(String message, String details) {
        super();
        this.timestamp = ZonedDateTime.now();
        this.message = message;
        this.details = details;
    }
}
