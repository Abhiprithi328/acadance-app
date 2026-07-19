package com.acadance.app.dto;

import jakarta.validation.constraints.NotBlank;

public class SubjectDto {

    public static class Request {
        @NotBlank
        private String name;
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }

    public static class Response {
        private Long id;
        private String name;

        public Response(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        public Long getId() { return id; }
        public String getName() { return name; }
    }
}
