package com.steven.willysbakeshop.configuration;

import org.springframework.http.MediaType;

public final class MediaTypes {

    private MediaTypes() {}

    public static final MediaType TEXT_CSV = new MediaType("text", "csv");
    public static final String TEXT_CSV_VALUE = "text/csv";
}
