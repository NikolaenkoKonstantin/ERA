package com.app.server.era.backend.services;

import com.app.server.era.backend.dto.UserLoginRequestDTO;
import com.app.server.era.backend.exceptions.AuthenticationBadRequestException;
import com.app.server.era.backend.exceptions.PatientDoesNotExistException;
import com.app.server.era.backend.models.*;
import com.app.server.era.backend.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AndroidService {
    private final PasswordEncoder passwordEncoder;
    private final DimensionRepository dimRepo;
    private final UserRepository userRepo;
    private final PatientRepository patRepo;
    private final NotificationRepository notRepo;


    public User login(UserLoginRequestDTO requestUserDTO){
        User user = userRepo.findByLogin(requestUserDTO.getLogin()).get();

        if(!passwordEncoder.matches(requestUserDTO.getPassword(), user.getPassword())){
            throw new AuthenticationBadRequestException("Wrong login or password");
        }

        return user;
    }


    @Transactional
    public String addDimension(String cardNumber, Dimension tempDim){
        String msg = "First measurement after surgery";
        Patient patient = patRepo.findPatientByCardNumber(cardNumber);

        if(patient != null) {
            EnrichDimension(tempDim, patient);

            Dimension beforeDim = dimRepo.findFirstByOwnerAndElbowKneeAndLeftRightOrderByDateTimeDesc(
                    patient,
                    tempDim.getElbowKnee(),
                    tempDim.getLeftRight()
            );

            if (beforeDim != null && beforeDim.getStatus().equals("rehabilitation")) {
                msg = checkRehabilitation(tempDim, beforeDim, patient);
            }

            dimRepo.save(tempDim);

            return msg;
        }
        else{
            throw new PatientDoesNotExistException("Error: patient does not exist");
        }
    }


    private String checkRehabilitation(Dimension tempDim, Dimension beforeDim, Patient patient){
        boolean result = true;

        if(((tempDim.getFlexionAngle() - beforeDim.getFlexionAngle()) < -2)
                || ((tempDim.getExtensionAngle() - beforeDim.getExtensionAngle()) < -2)
                || (tempDim.getCountBend() < beforeDim.getCountBend())
                || (tempDim.isDizziness())
                || (tempDim.getState() == 0)
                || (tempDim.getElbowKnee().equals("knee") && beforeDim.getDistance() > 0 && tempDim.getDistance() == 0)){
            result = false;
        }

        return setResponseMessage(result, patient);
    }


    private String setResponseMessage(boolean result, Patient patient){
        if(!result) {
            createNotification(patient);
            return "!The patient's condition worsened!";
        }else {
            return "Condition is normal";
        }
    }


    @Transactional
    public void createNotification(Patient patient){
        notRepo.save(new Notification(patient.getDoctor(), patient, LocalDateTime.now()));
    }


    private void EnrichDimension(Dimension dimension, Patient patient){
        dimension.setOwner(patient);
        dimension.setStatus("rehabilitation");
        dimension.setDateTime(LocalDateTime.now());
    }
}
