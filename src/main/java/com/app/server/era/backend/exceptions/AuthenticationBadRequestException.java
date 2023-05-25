package com.app.server.era.backend.exceptions;

public class AuthenticationBadRequestException extends RuntimeException{
    public AuthenticationBadRequestException(String msg){
        super(msg);
    }
}
