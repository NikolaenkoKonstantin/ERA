package com.app.server.era.ui.views.doctor;

import com.app.server.era.backend.dto.MessageSendRequest;
import com.app.server.era.backend.dto.PatientResponseDTO;
import com.app.server.era.backend.services.DoctorService;
import com.app.server.era.backend.services.EraEmailService;
import com.app.server.era.backend.utils.Converter;
import com.app.server.era.ui.utils.form.SendMessageEmailForm;
import com.app.server.era.ui.utils.layout.EraLayout;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

import static com.vaadin.flow.component.UI.getCurrent;


@RolesAllowed("ROLE_DOCTOR")
@Route(value = "/doctor/message", layout = EraLayout.class)
@PageTitle("Send message | ERA CRM")
public class SendMessageEmailView extends VerticalLayout {
    private final EraEmailService eraEmailService;
    private final Converter converter;
    private final DoctorService doctorService;
    SendMessageEmailForm form;
    MessageSendRequest sendMessage;
    PatientResponseDTO responseDTO;


    @Autowired
    public SendMessageEmailView(EraEmailService eraEmailService, Converter converter, DoctorService webService){
        this.eraEmailService = eraEmailService;
        this.converter = converter;
        this.doctorService = webService;

        addClassNames("sendMessageEmail-view");

        responseDTO = ComponentUtil.getData(getCurrent(), PatientResponseDTO.class);

        if(responseDTO != null) {
            getMessageSendRequest();
            configureForm();

            add(form);
        }else{
            add(new H3("Вы не выбрали пациента для редактирования"));
        }
    }


    private void getMessageSendRequest(){
        sendMessage = converter.convertToMessageSendRequest(doctorService.getEmailPatient(responseDTO.getId()));
    }


    private void configureForm() {
        form = new SendMessageEmailForm(sendMessage);

        form.setWidth("25em");
        form.addSaveListener(this::sendMessage);
        form.addCloseListener(this::closeForm);
    }


    private void closeForm(SendMessageEmailForm.CloseEvent closeEvent) {
        navigateToPatient(responseDTO);
    }


    private void sendMessage(SendMessageEmailForm.SendEvent sendEvent) {
        MessageSendRequest message = sendEvent.getMessageSendRequest();

        eraEmailService.send(message.getEmail(), "email", message.getMessage());

        navigateToPatient(responseDTO);
    }


    private void navigateToPatient(PatientResponseDTO dto){
        ComponentUtil.setData(UI.getCurrent(), PatientResponseDTO.class, dto);
        getUI().get().navigate("/doctor/patient");
    }
}
