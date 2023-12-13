package com.app.server.era.backend.services;

import com.app.server.era.backend.dto.CloseTreatmentRequest;
import com.app.server.era.backend.dto.PasswordEditRequest;
import com.app.server.era.backend.models.*;
import com.app.server.era.backend.repositories.*;
import com.app.server.era.backend.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DoctorService {
    private final PasswordEncoder passwordEncoder;
    private final DoctorRepository doctorRepo;
    private final NotificationRepository notRepo;
    private final DimensionRepository dimRepo;
    private final PatientRepository patRepo;
    private final UserRepository userRepo;


    public List<Dimension> findAllForCharts(int idPatient, String elbowKnee, String leftRight){
        Patient owner = patRepo.findById(idPatient).get();

        Dimension dimension = dimRepo.findFirstByOwnerAndElbowKneeAndLeftRightAndStatusOrderByDateTimeDesc(
                owner, elbowKnee, leftRight, "healthy");

        if(dimension != null) {
            return dimRepo.findAllByOwnerAndElbowKneeAndLeftRightAndDateTimeAfterOrderByDateTime(
                    owner, elbowKnee, leftRight, dimension.getDateTime());
        } else {
            return dimRepo.findAllByOwnerAndElbowKneeAndLeftRightOrderByDateTime(owner, elbowKnee, leftRight);
        }
    }


    @Transactional
    public Patient closeTreatment(CloseTreatmentRequest c){
        Patient patient = patRepo.findById(c.getId()).get();

        Dimension dimension = dimRepo.findFirstByOwnerAndElbowKneeAndLeftRightOrderByDateTimeDesc(
                patient,
                c.getElbowKnee().equalsIgnoreCase("Локтевой") ? "elbow" : "knee",
                c.getLeftRight().equalsIgnoreCase("Левая") ? "left" : "right");

        dimension.setStatus("healthy");
        dimRepo.save(dimension);

        return patient;
    }


    public String getEmailPatient(int id){
        return patRepo.findById(id).get().getUser().getLogin();
    }


    @Transactional
    public void deleteNotification(int id){
        notRepo.deleteById(id);
    }


    @Transactional
    public void EditPassword(PasswordEditRequest request){
        User user = patRepo.findById(request.getId()).get().getUser();
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepo.save(user);
    }


    @Transactional
    public Patient createPatient(Patient patient, User user){
        User userDoctor = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        patient.setDoctor(doctorRepo.findByUser(userDoctor));
        patient.setUser(user);
        return patRepo.save(patient);
    }


    public Doctor findDoctorByUser(User user){
        return doctorRepo.findByUser(user);
    }


    public Doctor findDoctorById(int id){
        return doctorRepo.findById(id).get();
    }


    public List<Patient> findAllPatientsByDoctor(
            String stringFilter, Doctor doctor) {
        if (stringFilter == null
                || stringFilter.isEmpty()) {
            return patRepo.findAllByDoctor(doctor);
        } else {
            return patRepo.search(stringFilter, doctor);
        }
    }


    public List<Dimension> findAllDimensionByPatient(int idPatient){
        return dimRepo.findAllByOwner(patRepo.findById(idPatient).get());
    }


    @Transactional
    public Patient updatePatient(Patient updatePatient){
        Patient patient = patRepo.findById(updatePatient.getId()).get();

        patient.setLastName(updatePatient.getLastName());
        patient.setFirstName(updatePatient.getFirstName());
        patient.setSurName(updatePatient.getSurName());
        patient.setAge(updatePatient.getAge());
        patient.setCardNumber(updatePatient.getCardNumber());

        return patRepo.save(patient);
    }


    public List<Notification> findAllNotificationByDoctor(Doctor doctor){
        return notRepo.findAllByDoctor(doctor);
    }


    public Patient findPatientByCardNumber(String cardNumber){
        return patRepo.findPatientByCardNumber(cardNumber);
    }
}
