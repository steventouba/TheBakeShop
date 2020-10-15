package com.steven.willysbakeshop.utilities.exceptions;

import java.util.ArrayList;
import java.util.List;

public class ValidationErrorResponse extends RuntimeException {
    private List<Violation> violations = new ArrayList<>();

    public void add(Violation violation)  {
        violations.add(violation);
    }

    @Override
    public String getMessage() {
        return "ValidationErrorResponse{" +
                "violations=" + violations +
                '}';
    }
}
