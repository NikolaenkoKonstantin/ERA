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

//Предстевление отправки сообщения
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


    //Конструктор
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


    //Получить сообщение
    private void getMessageSendRequest(){
        sendMessage = converter.convertToMessageSendRequest(doctorService.getEmailPatient(responseDTO.getId()));
    }


    //конфигурация формы
    private void configureForm() {
        form = new SendMessageEmailForm(sendMessage);

        form.setWidth("25em");
        form.addSaveListener(this::sendMessage);
        form.addCloseListener(this::closeForm);
    }


    //Закрытие формы
    private void closeForm(SendMessageEmailForm.CloseEvent closeEvent) {
        navigateToPatient(responseDTO);
    }


    //Вызов метода сервиса для отправки сообщения
    private void sendMessage(
            SendMessageEmailForm.SendEvent sendEvent) {
        //Получить сообщение из объекта sendEvent
        MessageSendRequest message =
                sendEvent.getMessageSendRequest();

        //Вызов метода собственного сервиса сообщений
        eraEmailService.send(
                message.getEmail(), "email", message.getMessage());

        //Переход на другое представление
        navigateToPatient(responseDTO);
    }


    //Переход на страницу пациента
    private void navigateToPatient(PatientResponseDTO dto){
        ComponentUtil.setData(UI.getCurrent(), PatientResponseDTO.class, dto);
        getUI().get().navigate(PatientView.class);
    }
}
