package com.app.server.era.backend.utils;

import com.app.server.era.backend.dto.*;
import com.app.server.era.backend.models.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

//Преобразователь из одного объекта в другой
@Component
@RequiredArgsConstructor
public class Converter {
    private final ModelMapper modelMapper;


    public PatientEditDoctorRequest convertToPatientEditDoctorRequest(PatientResponseDTO dto){
        return new PatientEditDoctorRequest(dto.getId());
    }


    public MessageSendRequest convertToMessageSendRequest(String email){
        return new MessageSendRequest(email);
    }


    public PasswordEditRequest convertToPasswordEditRequest(DoctorResponseDTO dto){
        return new PasswordEditRequest(dto.getId());
    }


    public PasswordEditRequest convertToPasswordEditRequest(PatientResponseDTO dto){
        return new PasswordEditRequest(dto.getId());
    }


    public DoctorRequestUpdateDTO convertToDoctorRequestUpdateDTO(DoctorResponseDTO dto){
        return modelMapper.map(dto, DoctorRequestUpdateDTO.class);
    }


    public Doctor convertToDoctor(DoctorRequestUpdateDTO dto){
        return modelMapper.map(dto, Doctor.class);
    }


    public User convertToUser(DoctorRequestCreateDTO dto){
        User user = new User();
        user.setLogin(dto.getLogin());
        user.setPassword(dto.getPassword());
        return user;
    }


    public Doctor convertToDoctor(DoctorRequestCreateDTO dto){
        return modelMapper.map(dto, Doctor.class);
    }


    public DoctorResponseDTO convertToDoctorResponseDTO(Doctor doctor){
        DoctorResponseDTO dto = modelMapper.map(doctor, DoctorResponseDTO.class);
        dto.setActive(doctor.getUser().isActive());
        return dto;
    }


    public Patient convertToPatient(PatientRequestCreateDTO dto){
        return modelMapper.map(dto, Patient.class);
    }


    public PatientResponseDTO convertToResponsePatientDTO(Patient patient){
        return modelMapper.map(patient, PatientResponseDTO.class);
    }


    public Dimension convertToDimension(DimensionRequestDTO dto){
        return modelMapper.map(dto, Dimension.class);
    }


    public User convertToUser(PatientRequestCreateDTO dto){
        User user = new User();
        user.setLogin(dto.getEmail());
        user.setPassword(dto.getPassword());
        return user;
    }


    public UserLoginResponseDTO convertToLoginResponseUserDTO(User user){
        return modelMapper.map(user, UserLoginResponseDTO.class);
    }


    public PatientRequestUpdateDTO convertToPatientRequestUpdateDTO(PatientResponseDTO dto){
        return modelMapper.map(dto, PatientRequestUpdateDTO.class);
    }


    public Patient convertToPatient(PatientRequestUpdateDTO dto){
        return modelMapper.map(dto, Patient.class);
    }


    public ScheduleRequest convertToScheduleRequest(DimensionResponseDTO dto){
        return new ScheduleRequest(
                dto.getElbowKnee().equals("Локтевой") ? "elbow" : "knee",
                dto.getLeftRight().equals("Левая") ? "left" : "right"
        );
    }


    public DimensionResponseDTO convertToResponseDimensionDTO(Dimension dimension){
        DimensionResponseDTO dto =  modelMapper.map(dimension, DimensionResponseDTO.class);

        dto.setDistance(dimension.getDistance() == null ? "Отсутствует" : ("" + dimension.getDistance()));
        dto.setStatus(dimension.getStatus().equals("healthy") ? "Здоров" : "Реабилитация");
        dto.setElbowKnee(dimension.getElbowKnee().equals("elbow") ? "Локтевой" : "Коленный");
        dto.setLeftRight(dimension.getLeftRight().equals("right") ? "Правая" : "Левая");

        return dto;
    }


    public CloseTreatmentRequest convertToClosingTreatmentRequest(PatientResponseDTO dto){
        return modelMapper.map(dto, CloseTreatmentRequest.class);
    }


    public NotificationResponseDTO convertToNotificationResponseDTO(Notification notification){
        return new NotificationResponseDTO(
                notification.getId(),
                notification.getPatient().getCardNumber(),
                notification.getDateTime()
        );
    }
}
