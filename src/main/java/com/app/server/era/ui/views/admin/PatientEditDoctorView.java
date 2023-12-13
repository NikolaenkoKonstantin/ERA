package com.app.server.era.ui.views.admin;

import com.app.server.era.backend.dto.PatientEditDoctorRequest;
import com.app.server.era.backend.dto.PatientResponseDTO;
import com.app.server.era.backend.services.AdminService;
import com.app.server.era.backend.utils.Converter;
import com.app.server.era.ui.utils.form.PatientEditDoctorForm;
import com.app.server.era.ui.utils.layout.EraLayout;
import com.app.server.era.ui.views.doctor.PatientView;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

import static com.vaadin.flow.component.UI.getCurrent;

@RolesAllowed("ROLE_ADMIN")
@Route(value = "/admin/patienEditDoctor", layout = EraLayout.class)
@PageTitle("Patient edit doctor | ERA CRM")
public class PatientEditDoctorView extends VerticalLayout {
    private final AdminService adminService;
    private final Converter converter;
    PatientEditDoctorForm form;
    PatientEditDoctorRequest editDoctor;
    PatientResponseDTO responseDTO;


    @Autowired
    public PatientEditDoctorView(AdminService adminService, Converter converter){
        this.adminService = adminService;
        this.converter = converter;

        addClassName("patientEditDoctor-view");

        responseDTO = ComponentUtil.getData(getCurrent(), PatientResponseDTO.class);

        if(responseDTO != null) {
            convertData();
            configureForm();
            add(form);
        }else{
            add(new H3("Вы не выбрали пациента у которого хотите закрыть лечение"));
        }
    }


    private void convertData(){
        editDoctor = converter.convertToPatientEditDoctorRequest(responseDTO);
    }


    private void configureForm() {
        form = new PatientEditDoctorForm(editDoctor, adminService.findAllDoctorsByLastName(null));

        form.setWidth("25em");
        form.addSaveListener(this::SaveEditDoctor);
        form.addCloseListener(this::closeForm);
    }


    private void SaveEditDoctor(PatientEditDoctorForm.SaveEvent saveEvent) {
        adminService.patientEditDoctor(saveEvent.getPatientEditDoctorRequest());
        navigateToPatient(responseDTO);
    }


    private void closeForm(PatientEditDoctorForm.CloseEvent event) {
        form.setBinder(null);
        navigateToPatient(responseDTO);
    }


    private void navigateToPatient(PatientResponseDTO dto){
        ComponentUtil.setData(UI.getCurrent(), PatientResponseDTO.class, dto);
        getUI().get().navigate(PatientView.class);
    }
}
