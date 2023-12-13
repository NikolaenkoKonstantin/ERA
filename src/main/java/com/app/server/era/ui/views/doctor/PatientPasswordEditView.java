package com.app.server.era.ui.views.doctor;

import com.app.server.era.backend.dto.PasswordEditRequest;
import com.app.server.era.backend.dto.PatientResponseDTO;
import com.app.server.era.backend.services.DoctorService;
import com.app.server.era.backend.utils.Converter;
import com.app.server.era.ui.utils.form.PasswordEditForm;
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

@RolesAllowed("ROLE_DOCTOR")
@Route(value = "/doctor/password", layout = EraLayout.class)
@PageTitle("Edit password | ERA CRM")
public class PatientPasswordEditView extends VerticalLayout {
    private final Converter converter;
    private final DoctorService doctorService;
    PasswordEditForm form;
    PatientResponseDTO responseDTO;
    PasswordEditRequest editPassword;


    @Autowired
    public PatientPasswordEditView(Converter converter, DoctorService webService){
        this.converter = converter;
        this.doctorService = webService;

        responseDTO = ComponentUtil.getData(getCurrent(), PatientResponseDTO.class);

        if(responseDTO != null) {
            convertData();

            addClassName("patientPasswordEdit-view");
            configureForm(editPassword);

            add(form);
        }else{
            add(new H3("Не выбран пациент для изменения пароля"));
        }
    }


    private void convertData() {
        editPassword = converter.convertToPasswordEditRequest(responseDTO);
    }


    private void configureForm(PasswordEditRequest request) {
        form = new PasswordEditForm(request);

        form.setWidth("25em");
        form.addSaveListener(this::savePassword);
        form.addCloseListener(event -> navigateToPatient());
    }


    private void savePassword(PasswordEditForm.SaveEvent event) {
        doctorService.EditPassword(event.getPasswordEditRequest());
        navigateToPatient();
    }


    private void navigateToPatient(){
        ComponentUtil.setData(UI.getCurrent(), PatientResponseDTO.class, responseDTO);
        getUI().get().navigate(PatientView.class);
    }
}
