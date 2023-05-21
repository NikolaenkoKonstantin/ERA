package com.app.server.era.backend.exceptions;

public class AuthorizedException extends RuntimeException{
    public AuthorizedException(String msg){
        super(msg);
    }
}
