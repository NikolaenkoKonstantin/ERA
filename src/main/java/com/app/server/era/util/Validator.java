package com.app.server.era.util;

import com.app.server.era.Exception.DimensionDTOBadRequestException;
import com.app.server.era.Exception.AuthorizedBadRequestException;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

@Component
public class Validator {

    public void loginUserValidation(BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            StringBuilder errorMsg = createErrorMsg(bindingResult);
            throw new AuthorizedBadRequestException(errorMsg.toString());
        }
    }

    public void dimensionValidation(BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            StringBuilder errorMsg = createErrorMsg(bindingResult);
            throw new DimensionDTOBadRequestException(errorMsg.toString());
        }
    }

    private StringBuilder createErrorMsg(BindingResult bindingResult){
        StringBuilder errorMsg = new StringBuilder();
        List<FieldError> errors = bindingResult.getFieldErrors();

        for(FieldError error : errors)
            errorMsg.append(error.getField())
                    .append(" - ")
                    .append(error.getDefaultMessage())
                    .append(";");

        return errorMsg;
    }
}
