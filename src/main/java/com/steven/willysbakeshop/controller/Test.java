package com.steven.willysbakeshop.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.bind.annotation.XmlRootElement;

@RestController
@RequestMapping("/test")
//@Transactional
public class Test {

    @GetMapping(value = "/ping", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Foo> ping() {
        return ResponseEntity.ok(new Foo("PONG"));
    }

    @JacksonXmlRootElement(localName = "Foo")
    public static class Foo {

        @JsonProperty
        private String message;

        public Foo(String message) {
            this.message = message;
        }
    }
}
