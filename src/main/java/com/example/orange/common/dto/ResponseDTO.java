package com.example.orange.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
public class ResponseDTO<T> {
    private String message;
    private HttpStatus statusCode;
    private T data;

    public static <T> ResponseDTO<T> success(String message, T data,HttpStatus statusCode) {
        return new ResponseDTO<>(message, statusCode, data);
    }

    public static <T> ResponseDTO<T> failure(String message, HttpStatus statusCode) {
        return new ResponseDTO<>(message, statusCode, null);
    }
}