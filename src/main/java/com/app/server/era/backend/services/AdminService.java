package com.app.server.era.backend.services;

import com.app.server.era.backend.dto.PasswordEditRequest;
import com.app.server.era.backend.dto.PatientEditDoctorRequest;
import com.app.server.era.backend.models.Doctor;
import com.app.server.era.backend.models.Patient;
import com.app.server.era.backend.models.User;
import com.app.server.era.backend.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminService {
    private final PasswordEncoder passwordEncoder;
    private final PatientRepository patRepo;
    private final UserRepository userRepo;
    private final DoctorRepository doctorRepo;


    //Получение всех докторов по фамилии
    public List<Doctor> findAllDoctorsByLastName(String stringFilter){
        if (stringFilter == null || stringFilter.isEmpty()) {
            return doctorRepo.findAll();
        } else {
            return doctorRepo.search(stringFilter);
        }
    }


    //Получение доктора по id
    public Doctor findDoctorById(int id){
        return doctorRepo.findById(id).get();
    }


    //Смена доктора у пациента (админом)
    @Transactional
    public void patientEditDoctor(PatientEditDoctorRequest dto){
        Patient patient = patRepo.findById(dto.getId()).get();
        patient.setDoctor(dto.getDoctor());
        patRepo.save(patient);
    }


    //Изменение пароля доктора (админом)
    @Transactional
    public void EditPassword(PasswordEditRequest request){
        User user = doctorRepo.findById(request.getId()).get().getUser();
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepo.save(user);
    }


    //Блокировка аккаунта доктора (админом)
    @Transactional
    public void blockDoctor(int id){
        User user = doctorRepo.findById(id).get().getUser();
        user.setActive(false);
        userRepo.save(user);
    }


    //Разблокировка аккаунта доктора (админом)
    @Transactional
    public void unlockDoctor(int id){
        User user = doctorRepo.findById(id).get().getUser();
        user.setActive(true);
        userRepo.save(user);
    }


    //Создание аккаунта доктора (админом)
    @Transactional
    public Doctor createDoctor(Doctor doctor, User user){
        doctor.setUser(user);
        return doctorRepo.save(doctor);
    }


    //Изменение данных доктора (админом)
    @Transactional
    public Doctor updateDoctor(Doctor updateDoctor){
        Doctor doctor = doctorRepo.findById(updateDoctor.getId()).get();

        doctor.setLastName(updateDoctor.getLastName());
        doctor.setFirstName(updateDoctor.getFirstName());
        doctor.setSurName(updateDoctor.getSurName());

        return doctorRepo.save(doctor);
    }


}
