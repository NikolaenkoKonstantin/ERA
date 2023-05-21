package com.app.server.era.ui.views.doctor;


import com.app.server.era.backend.dto.PatientRequestCreateDTO;
import com.app.server.era.ui.utils.form.PatientCreateForm;
import com.app.server.era.ui.utils.layout.EraLayout;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import jakarta.annotation.security.RolesAllowed;

@RolesAllowed("ROLE_DOCTOR")
@Route(value = "/doctor/create", layout = EraLayout.class)
@PageTitle("Create patient | ERA CRM")
public class PatientCreateView extends VerticalLayout {
    PatientCreateForm form;


    public PatientCreateView(){
        addClassName("patientCreate-view");

        configureForm();

        add(form);
    }


    private void configureForm() {
        form = new PatientCreateForm();

        form.setWidth("25em");
        form.addSaveListener(this::navigateToEmailConfirmation);
        form.addCloseListener(event -> navigateToPatients());
    }


    private void navigateToPatients() {
        form.setBinder(null);
        getUI().get().navigate("/doctor/patients");
    }


    private void navigateToEmailConfirmation(PatientCreateForm.SaveEvent event){
        PatientRequestCreateDTO dto = event.getRequestPatientDTO();

        ComponentUtil.setData(UI.getCurrent(), PatientRequestCreateDTO.class, dto);
        getUI().get().navigate(EmailConfirmationView.class);
    }
}
