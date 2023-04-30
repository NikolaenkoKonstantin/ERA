package com.app.server.era.Exception;

public class AuthorizedBadRequestException extends RuntimeException{
    public AuthorizedBadRequestException(String msg){
        super(msg);
    }
}
