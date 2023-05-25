package com.app.server.era.backend.restControllers;

import com.app.server.era.backend.dto.DimensionRequestDTO;
import com.app.server.era.backend.dto.UserLoginRequestDTO;
import com.app.server.era.backend.dto.UserLoginResponseDTO;
import com.app.server.era.backend.exceptions.*;
import com.app.server.era.backend.services.AndroidService;
import com.app.server.era.backend.utils.Converter;
import com.app.server.era.backend.utils.HandlerException;
import com.app.server.era.backend.utils.Validator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

//Контроллер (web-служба)
@org.springframework.web.bind.annotation.RestController
@RequiredArgsConstructor
@RequestMapping("/android")
public class RestController {
    private final AndroidService androidService;
    private final Converter converter;
    private final Validator validator;

    //Получаем логин и пароль от аккаунта
    // пациента для доступа к андроид приложению
    @GetMapping("/login")
    public ResponseEntity<UserLoginResponseDTO> login(
            @RequestBody @Valid UserLoginRequestDTO dto,
            BindingResult bindingResult){
        validator.loginUserValidation(bindingResult);

        return ResponseEntity.ok(
                converter.convertToLoginResponseUserDTO(
                        androidService.login(dto)));
    }


    //Отлавливает исключение неверного
    //логина или пароля для аутентификации
    @ExceptionHandler
    private ResponseEntity<AuthenticationError> handleException(
            AuthenticationException ex){
        return new HandlerException().handleException(ex);
    }


    //Отлавливает исключение неправильно
    //заданного запроса на аутентификацию
    @ExceptionHandler
    private ResponseEntity<AuthenticationError> handleException(
            AuthenticationBadRequestException ex){
        return new HandlerException().handleException(ex);
    }


    //Получение данных об измерении от андроида
    @PostMapping("/dimension")
    public ResponseEntity<String> addDimension(
            @RequestBody @Valid DimensionRequestDTO dto,
            BindingResult bindingResult) {
        validator.dimensionValidation(bindingResult);

        return ResponseEntity.ok(androidService.addDimension(
                dto.getCardNumber(),
                converter.convertToDimension(dto)));
    }


    //Отлавливает исключение на
    //неправильный запрос добавления измерений
    @ExceptionHandler
    private ResponseEntity<DimensionDTOErrorResponse>
    handleException(DimensionDTOBadRequestException ex){
        return new HandlerException().handleException(ex);
    }


    //Отлавливает исключение на не найденного
    //пациента, которому хотели добавить измерение
    @ExceptionHandler
    private ResponseEntity<PatientDoesNotExistError>
    handleException(PatientDoesNotExistException ex){
        return new HandlerException().handleException(ex);
    }
}


