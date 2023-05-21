package com.app.server.era.backend.exceptions;

public class AuthorizedBadRequestException extends RuntimeException{
    public AuthorizedBadRequestException(String msg){
        super(msg);
    }
}
