package com.app.server.era.service;

import com.app.server.era.Exception.PatientDoesNotExistException;
import com.app.server.era.model.Dimension;
import com.app.server.era.model.Patient;
import com.app.server.era.repository.DimensionRepository;
import com.app.server.era.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DimensionService {
    private final DimensionRepository dimRepo;
    private final PatientRepository patRepo;

    @Transactional
    public void addDimension(String cardNumber, Dimension tempDim){
        Patient patient = patRepo.findPatientByCardNumber(cardNumber);

        if(patient != null) {
            EnrichDimension(tempDim, patient);

            Dimension beforeDim = dimRepo.findFirstByOwnerOrderByDateTimeDesc(patient);
            if (beforeDim != null)
                System.out.println(checkRehabilitation(tempDim, beforeDim));

            dimRepo.save(tempDim);
        }
        else{
            throw new PatientDoesNotExistException("Error: patient does not exist");
        }
    }

    private boolean checkRehabilitation(Dimension tempDim, Dimension beforeDim){
        boolean result = true;

        //допускаем погрешность измерений в 2 градуса
        if(((beforeDim.getFlexionAngle() - tempDim.getFlexionAngle()) < -2)
                || ((beforeDim.getExtensionAngle() - tempDim.getExtensionAngle()) < -2)
                || (tempDim.getCountBend() < beforeDim.getCountBend())
                || (tempDim.isDizziness())
                || (tempDim.getState() == 0)){
            //по факту здесь будет функционал отправки сообщения
            result = false;
        }

        //отдельная проверка на дистанцию для колена (не уверн что нужна)
        else if(tempDim.getElbowKnee() == 1 && beforeDim.getDistance() > 0 && tempDim.getDistance() == 0){
            //по факту здесь будет функционал отправки сообщения
            result = false;
        }

        return result;
    }

    private void EnrichDimension(Dimension dimension, Patient patient){
        dimension.setOwner(patient);
        dimension.setDateTime(LocalDateTime.now());
    }
}
