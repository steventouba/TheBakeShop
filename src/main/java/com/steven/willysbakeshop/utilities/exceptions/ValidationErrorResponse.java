package com.steven.willysbakeshop.utilities.exceptions;

import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class ValidationErrorResponse {
    private List<Violation> violations = new ArrayList<>();
    private final ZonedDateTime timestamp = ZonedDateTime.now();
    private final HttpStatus status = HttpStatus.BAD_REQUEST;
    private final String error = "Bad Request";

    public void add(Violation violation)  {
        violations.add(violation);
    }

    public String getMessage() {
        return violations.toString();
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status.value();
    }

    public String getError() {
        return error;
    }
}
