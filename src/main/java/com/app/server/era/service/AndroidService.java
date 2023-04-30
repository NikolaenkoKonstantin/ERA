package com.app.server.era.service;

import com.app.server.era.DTO.LoginRequestUserDTO;
import com.app.server.era.Exception.AuthorizedBadRequestException;
import com.app.server.era.Exception.PatientDoesNotExistException;
import com.app.server.era.model.Dimension;
import com.app.server.era.model.Patient;
import com.app.server.era.model.User;
import com.app.server.era.repository.DimensionRepository;
import com.app.server.era.repository.PatientRepository;
import com.app.server.era.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AndroidService {
    private final DimensionRepository dimRepo;
    private final UserRepository userRepo;
    private final PatientRepository patRepo;

    public User login(LoginRequestUserDTO requestUserDTO){
        User user = userRepo.findByLoginAndPassword(requestUserDTO.getLogin(), requestUserDTO.getPassword());

        if(user == null){
            throw new AuthorizedBadRequestException("Wrong login or password");
        }

        return user;
    }

    @Transactional
    public String addDimension(String cardNumber, Dimension tempDim){
        String msg = "First measurement after surgery";
        Patient patient = patRepo.findPatientByCardNumber(cardNumber);

        if(patient != null) {
            EnrichDimension(tempDim, patient);

            Dimension beforeDim = dimRepo.findFirstByOwnerAndElbowKneeOrderByDateTimeDesc(patient, tempDim.getElbowKnee());
            //Если есть предыдущее измерение, то сравниваем с ним
            if (beforeDim != null && beforeDim.getStatus() == 0) {
                msg = checkRehabilitation(tempDim, beforeDim);
            }

            dimRepo.save(tempDim);

            return msg;
        }
        else{
            throw new PatientDoesNotExistException("Error: patient does not exist");
        }
    }


    private String checkRehabilitation(Dimension tempDim, Dimension beforeDim){
        boolean result = true;

        if(((tempDim.getFlexionAngle() - beforeDim.getFlexionAngle()) < -1)
                || ((tempDim.getExtensionAngle() - beforeDim.getExtensionAngle()) < -1)
                || (tempDim.getCountBend() < beforeDim.getCountBend())
                || (tempDim.isDizziness())
                || (tempDim.getState() == 0)
                || (tempDim.getElbowKnee() == 1 && beforeDim.getDistance() > 0 && tempDim.getDistance() == 0)){
            result = false;
        }

        return sendMessage(result);
    }

    private String sendMessage(boolean result){
        if(!result) {
            //отправить сообщение врачу TODO
            return "The patient's condition worsened";
        }else {
            return "Condition is normal";
        }
    }

    private void EnrichDimension(Dimension dimension, Patient patient){
        dimension.setOwner(patient);
        dimension.setStatus(0);
        dimension.setDateTime(LocalDateTime.now());
    }
}
