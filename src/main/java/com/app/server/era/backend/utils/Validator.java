package com.app.server.era.backend.utils;

import com.app.server.era.backend.exceptions.DimensionDTOBadRequestException;
import com.app.server.era.backend.exceptions.AuthenticationBadRequestException;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

//Валидатор данных для REST части
@Component
public class Validator {
    public void loginUserValidation(
            BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            StringBuilder errorMsg =
                    createErrorMsg(bindingResult);
            throw new AuthenticationBadRequestException(
                    errorMsg.toString());
        }
    }


    public void dimensionValidation(
            BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            StringBuilder errorMsg =
                    createErrorMsg(bindingResult);
            throw new DimensionDTOBadRequestException(
                    errorMsg.toString());
        }
    }


    //Проверка на ошибки
    private StringBuilder createErrorMsg(
            BindingResult bindingResult){
        StringBuilder errorMsg = new StringBuilder();
        List<FieldError> errors =
                bindingResult.getFieldErrors();

        for(FieldError error : errors)
            errorMsg.append(error.getField())
                    .append(" - ")
                    .append(error.getDefaultMessage())
                    .append(";");

        return errorMsg;
    }
}
