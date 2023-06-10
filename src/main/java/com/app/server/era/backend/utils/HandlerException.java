package com.app.server.era.backend.utils;


import com.app.server.era.backend.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

//Обработчик исключений, создание ответов в виде ошибок
public class HandlerException {
    public ResponseEntity<AuthenticationError>
    handleException(AuthenticationException ex){
        AuthenticationError response =
                new AuthenticationError(ex.getMessage());
        return new ResponseEntity<>(
                response, HttpStatus.UNAUTHORIZED);
    }


    public ResponseEntity<AuthenticationError>
    handleException(AuthenticationBadRequestException ex){
        AuthenticationError response =
                new AuthenticationError(ex.getMessage());
        return new ResponseEntity<>(
                response, HttpStatus.BAD_REQUEST);
    }


    public ResponseEntity<DimensionDTOErrorResponse>
    handleException(DimensionDTOBadRequestException ex){
        DimensionDTOErrorResponse response =
                new DimensionDTOErrorResponse(
                        ex.getMessage(),
                        System.currentTimeMillis());
        return new ResponseEntity<>(
                response, HttpStatus.BAD_REQUEST);
    }


    public ResponseEntity<PatientDoesNotExistError>
    handleException(PatientDoesNotExistException ex){
        PatientDoesNotExistError response =
                new PatientDoesNotExistError(
                        ex.getMessage(),
                        System.currentTimeMillis());
        return new ResponseEntity<>(
                response, HttpStatus.BAD_REQUEST);
    }
}
