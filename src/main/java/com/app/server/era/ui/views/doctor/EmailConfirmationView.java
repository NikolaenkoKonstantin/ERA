package com.app.server.era.ui.views.doctor;

import com.app.server.era.backend.dto.PatientRequestCreateDTO;
import com.app.server.era.backend.dto.PatientResponseDTO;
import com.app.server.era.backend.services.DoctorService;
import com.app.server.era.backend.services.EraEmailService;
import com.app.server.era.backend.services.RegistrationService;
import com.app.server.era.backend.utils.Converter;
import com.app.server.era.ui.utils.form.EmailConfirmationForm;
import com.app.server.era.ui.utils.layout.EraLayout;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

import static com.vaadin.flow.component.UI.getCurrent;

//Представление подтверждения почты
@RolesAllowed("ROLE_DOCTOR")
@Route(value = "/confirmation", layout = EraLayout.class)
@PageTitle("Email confirmation | ERA CRM")
public class EmailConfirmationView extends VerticalLayout {
    private final DoctorService doctorService;
    private final RegistrationService regService;
    private final Converter converter;
    private final EraEmailService eraEmailService;
    EmailConfirmationForm form;
    String codeServer;
    PatientRequestCreateDTO dto;


    //Конструктор
    @Autowired
    public EmailConfirmationView(DoctorService doctorService, RegistrationService regService,
                                 Converter converter, EraEmailService eraEmailService){
        this.doctorService = doctorService;
        this.regService = regService;
        this.converter = converter;
        this.eraEmailService = eraEmailService;

        dto = ComponentUtil.getData(getCurrent(), PatientRequestCreateDTO.class);

        if(dto != null) {
            sendCodeToEmail();
            configureForm();

            add(form);
        }else {
            add(new H3("Без ввода данных нового пациента в доступе отказано"));
        }
    }


    //Конфигурация формы
    private void configureForm() {
        form = new EmailConfirmationForm();

        form.setWidth("25em");
        form.addSendListener(this::checkCode);
        form.addCloseListener(e -> closeForm());
    }


    //Закрытие формы
    private void closeForm() {
        getUI().get().navigate(PatientCreateView.class);
    }


    //Проверка кода
    private void checkCode(EmailConfirmationForm.SendEvent sendEvent) {
        String codeClient = sendEvent.getEmailCodeRequest().getCode();

        if(codeClient.equals(codeServer)){
            savePatient();
        }else{
            closeForm();
        }
    }


    //Сохранение пациента
    private void savePatient() {
        navigateToPatient(converter.convertToResponsePatientDTO(doctorService.createPatient(
                converter.convertToPatient(dto), regService.registratePatient(converter.convertToUser(dto)))));
    }


    //Отправка сообщения с кодом на почту
    private void sendCodeToEmail() {
        codeServer = eraEmailService.sendCodeToEmail(dto.getEmail());
    }


    //Переход на страницу пациента
    private void navigateToPatient(PatientResponseDTO dto){
        ComponentUtil.setData(UI.getCurrent(), PatientResponseDTO.class, dto);
        getUI().get().navigate(PatientView.class);
    }
}
