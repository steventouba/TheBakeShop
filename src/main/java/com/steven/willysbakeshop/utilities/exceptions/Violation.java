package com.steven.willysbakeshop.utilities.exceptions;

import java.util.Arrays;
import java.util.Comparator;

public class Violation {
    private final String fieldName;
    private final String message;

    public Violation(String fieldName, String message) {
        this.fieldName = fieldName;
        this.message = message;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return fieldName + ": " + message;
    }

    public void foo() {
        int[][] intervals = { {3,10},{4,10},{5,11} };

        Arrays.sort(intervals, Comparator.<int[]>comparingInt(o -> o[1]).thenComparingInt(o -> o[0]));

    }
}
