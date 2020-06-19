package com.karasik.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class ErrorDto {

    @Getter
    private final int status;

    @Getter
    private final String message;

    private ErrorDto(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public static ErrorDto getException(int statusCode, String message) {
        return new ErrorDto(statusCode, message);
    }
}
