package com.app.server.era.backend.restControllers;

import com.app.server.era.backend.dto.DimensionRequestDTO;
import com.app.server.era.backend.dto.UserLoginRequestDTO;
import com.app.server.era.backend.dto.UserLoginResponseDTO;
import com.app.server.era.backend.exceptions.*;
import com.app.server.era.backend.models.Dimension;
import com.app.server.era.backend.models.Patient;
import com.app.server.era.backend.repositories.DimensionRepository;
import com.app.server.era.backend.repositories.PatientRepository;
import com.app.server.era.backend.services.AndroidService;
import com.app.server.era.backend.utils.Converter;
import com.app.server.era.backend.utils.HandlerException;
import com.app.server.era.backend.utils.Validator;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequiredArgsConstructor
@RequestMapping("/android")
public class AndroidRestController {
    private final AndroidService androidService;
    private final Converter converter;
    private final Validator validator;

    //Получаем логин и пароль от аккаунта пациента для доступа к андроид приложению
    @GetMapping("/login")
    public ResponseEntity<UserLoginResponseDTO> login(@RequestBody @Valid UserLoginRequestDTO dto,
                                                      BindingResult bindingResult){
        validator.loginUserValidation(bindingResult);

        return ResponseEntity.ok(converter.convertToLoginResponseUserDTO(androidService.login(dto)));
    }


    @ExceptionHandler
    private ResponseEntity<AuthorizedError> handleException(AuthorizedException ex){
        return new HandlerException().handleException(ex);
    }


    @ExceptionHandler
    private ResponseEntity<AuthorizedError> handleException(AuthorizedBadRequestException ex){
        return new HandlerException().handleException(ex);
    }


    //Получение данных об измерении от андроида
    @PostMapping("/dimension")
    public ResponseEntity<String> addDimension(@RequestBody @Valid DimensionRequestDTO dto, BindingResult bindingResult) {
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


