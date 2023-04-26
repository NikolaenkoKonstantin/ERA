package com.app.server.era.restController;

import com.app.server.era.DTO.DimensionDTO;
import com.app.server.era.Exception.DimensionDTOBadRequestException;
import com.app.server.era.Exception.DimensionDTOErrorResponse;
import com.app.server.era.Exception.PatientDoesNotExistError;
import com.app.server.era.Exception.PatientDoesNotExistException;
import com.app.server.era.service.DimensionService;
import com.app.server.era.util.Converter;
import com.app.server.era.util.HandlerException;
import com.app.server.era.util.Validator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class DimensionRestController {
    private final DimensionService dimService;
    private final Converter converter;
    private final Validator validator;

    @PostMapping("/dimension")
    public ResponseEntity<HttpStatus> addDimension(@RequestBody @Valid DimensionDTO dto, BindingResult bindingResult) {
        validator.dimensionValidation(bindingResult);

        dimService.addDimension(dto.getCardNumber(), converter.convertToDimension(dto));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<DimensionDTOErrorResponse> handleException(DimensionDTOBadRequestException ex){
        return new HandlerException().handleException(ex);
    }

    @ExceptionHandler
    private ResponseEntity<PatientDoesNotExistError> handleException(PatientDoesNotExistException ex){
        return new HandlerException().handleException(ex);
    }

}
