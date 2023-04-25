package com.app.server.era.util;


import com.app.server.era.Exception.DimensionDTOBadRequestException;
import com.app.server.era.Exception.DimensionDTOErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class HandlerException {

    public ResponseEntity<DimensionDTOErrorResponse> handleException(DimensionDTOBadRequestException ex){
        DimensionDTOErrorResponse response = new DimensionDTOErrorResponse(ex.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
