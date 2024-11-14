package com.workintech.zoo.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ZooException extends RuntimeException{
    HttpStatus status;

    public ZooException(String message,  HttpStatus status) {
        super(message);
        this.status = status;
    }
}
