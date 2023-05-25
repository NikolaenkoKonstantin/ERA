package com.app.server.era.ui.views.doctor;

import com.app.server.era.backend.dto.CloseTreatmentRequest;
import com.app.server.era.backend.dto.PatientResponseDTO;
import com.app.server.era.backend.services.DoctorService;
import com.app.server.era.backend.utils.Converter;
import com.app.server.era.ui.utils.form.ClosingTreatmentForm;
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

//Представление закрытия лечения
@RolesAllowed("ROLE_DOCTOR")
@Route(value = "/doctor/closing", layout = EraLayout.class)
@PageTitle("Closing treatment| ERA CRM")
public class ClosingTreatmentView extends VerticalLayout {
    private final DoctorService doctorService;
    private final Converter converter;
    ClosingTreatmentForm form;
    CloseTreatmentRequest closeDTO;
    PatientResponseDTO responseDTO;


    //Конструктор
    @Autowired
    public ClosingTreatmentView(DoctorService doctorService, Converter converter){
        this.doctorService = doctorService;
        this.converter = converter;

        addClassName("closingTreatment-view");

        responseDTO = ComponentUtil.getData(getCurrent(), PatientResponseDTO.class);

        if(responseDTO != null) {
            convertData();
            configureForm();
            add(form);
        }else{
            add(new H3("Вы не выбрали пациента у которого хотите закрыть лечение"));
        }
    }


    //Преобразователь данных с помощью конвертера
    private void convertData() {
        closeDTO = converter.convertToClosingTreatmentRequest(responseDTO);
    }


    //Конфигурация формы
    private void configureForm() {
        form = new ClosingTreatmentForm();

        form.setBinder(closeDTO);

        form.setWidth("25em");
        form.addCloseTreatmentListener(this::closeTreatment);
        form.addCloseListener(this::closeForm);
    }


    //Закрытия формы
    private void closeForm(ClosingTreatmentForm.CloseEvent event) {
        form.setBinder(null);
        navigateToPatient(responseDTO);
    }


    //Закрытие лечения
    private void closeTreatment(ClosingTreatmentForm.CloseTreatmentEvent event) {
        navigateToPatient(converter.convertToResponsePatientDTO(
                doctorService.closeTreatment(event.getCloseTreatmentRequest())));
    }


    //Переход на страницу пациента
    private void navigateToPatient(PatientResponseDTO dto){
        ComponentUtil.setData(UI.getCurrent(), PatientResponseDTO.class, dto);
        getUI().get().navigate(PatientView.class);
    }
}
