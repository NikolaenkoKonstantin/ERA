package com.app.server.era.backend.exceptions;

public class PatientDoesNotExistException extends RuntimeException {
    public PatientDoesNotExistException(String msg){
        super(msg);
    }
}
