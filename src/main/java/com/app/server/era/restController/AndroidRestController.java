package com.app.server.era.restController;

import com.app.server.era.DTO.DimensionDTO;
import com.app.server.era.DTO.LoginRequestUserDTO;
import com.app.server.era.DTO.LoginResponseUserDTO;
import com.app.server.era.Exception.*;
import com.app.server.era.service.AndroidService;
import com.app.server.era.util.Converter;
import com.app.server.era.util.HandlerException;
import com.app.server.era.util.Validator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/android")
public class AndroidRestController {
    private final AndroidService androidService;
    private final Converter converter;
    private final Validator validator;

    @GetMapping("/login")
    public ResponseEntity<LoginResponseUserDTO> login(@RequestBody @Valid LoginRequestUserDTO dto,
                                                      BindingResult bindingResult){
        validator.loginUserValidation(bindingResult);

        return ResponseEntity.ok(converter.convertToLoginResponseUserDTO(androidService.login(dto)));
    }

    @ExceptionHandler
    private ResponseEntity<AuthorizedError> handleException(AuthorizedExceprion ex){
        return new HandlerException().handleException(ex);
    }

    @ExceptionHandler
    private ResponseEntity<AuthorizedError> handleException(AuthorizedBadRequestException ex){
        return new HandlerException().handleException(ex);
    }

    @PostMapping("/dimension")
    public ResponseEntity<String> addDimension(@RequestBody @Valid DimensionDTO dto, BindingResult bindingResult) {
        validator.dimensionValidation(bindingResult);

        return ResponseEntity.ok(androidService.addDimension(dto.getCardNumber(), converter.convertToDimension(dto)));
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
