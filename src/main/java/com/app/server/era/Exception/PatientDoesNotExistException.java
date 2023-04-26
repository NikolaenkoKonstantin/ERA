package com.app.server.era.Exception;

public class PatientDoesNotExistException extends RuntimeException {
    public PatientDoesNotExistException(String msg){
        super(msg);
    }
}
