package com.app.server.era.ui.views.doctor;

import com.app.server.era.backend.dto.PatientRequestUpdateDTO;
import com.app.server.era.backend.dto.PatientResponseDTO;
import com.app.server.era.backend.services.DoctorService;
import com.app.server.era.backend.utils.Converter;
import com.app.server.era.ui.utils.form.PatientUpdateForm;
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

//Представление редактирования пациента
@RolesAllowed("ROLE_DOCTOR")
@Route(value = "/doctor/update", layout = EraLayout.class)
@PageTitle("Update patient | ERA CRM")
public class PatientUpdateView extends VerticalLayout {
    private final Converter converter;
    private final DoctorService doctorService;
    PatientUpdateForm form;
    PatientRequestUpdateDTO updateDTO;
    PatientResponseDTO responseDTO;


    //Конструктор
    @Autowired
    public PatientUpdateView(Converter converter, DoctorService webService){
        this.converter = converter;
        this.doctorService = webService;

        addClassNames("patientUpdate-view");

        responseDTO = ComponentUtil.getData(getCurrent(), PatientResponseDTO.class);

        if(responseDTO != null) {
            convertData();
            configureForm(updateDTO);

            add(form);
        }else{
            add(new H3("Вы не выбрали пациента для редактирования"));
        }
    }


    //Преобразоваине в нужные данные с помощью конвертера
    private void convertData() {
        updateDTO = converter.convertToPatientRequestUpdateDTO(responseDTO);
    }


    //Конфигурация формы
    private void configureForm(PatientRequestUpdateDTO editPatient) {
        form = new PatientUpdateForm(editPatient);

        form.setWidth("25em");
        form.addSaveListener(this::savePatient);
        form.addCloseListener(this::closeForm);
    }


    //Закрытие формы
    private void closeForm(PatientUpdateForm.CloseEvent closeEvent) {
        form.setBinder(null);
        navigateToPatient(responseDTO);
    }


    //Сохранение изменений данных пациента
    private void savePatient(PatientUpdateForm.SaveEvent event) {
        navigateToPatient(converter.convertToResponsePatientDTO(doctorService.updatePatient(
                converter.convertToPatient(event.getPatientRequestUpdateDTO()))));
    }


    //Переход на страницу пациента
    private void navigateToPatient(PatientResponseDTO dto){
        ComponentUtil.setData(UI.getCurrent(), PatientResponseDTO.class, dto);
        getUI().get().navigate(PatientView.class);
    }
}
