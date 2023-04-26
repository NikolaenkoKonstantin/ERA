package com.app.server.era.util;


import com.app.server.era.Exception.DimensionDTOBadRequestException;
import com.app.server.era.Exception.DimensionDTOErrorResponse;
import com.app.server.era.Exception.PatientDoesNotExistError;
import com.app.server.era.Exception.PatientDoesNotExistException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class HandlerException {

    public ResponseEntity<DimensionDTOErrorResponse> handleException(DimensionDTOBadRequestException ex){
        DimensionDTOErrorResponse response = new DimensionDTOErrorResponse(ex.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<PatientDoesNotExistError> handleException(PatientDoesNotExistException ex){
        PatientDoesNotExistError response = new PatientDoesNotExistError(ex.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
