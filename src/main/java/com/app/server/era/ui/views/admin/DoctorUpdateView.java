package com.app.server.era.ui.views.admin;

import com.app.server.era.backend.dto.DoctorRequestUpdateDTO;
import com.app.server.era.backend.dto.DoctorResponseDTO;
import com.app.server.era.backend.services.AdminService;
import com.app.server.era.backend.utils.Converter;
import com.app.server.era.ui.utils.form.DoctorUpdateForm;
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

@RolesAllowed("ROLE_ADMIN")
@Route(value = "/admin/update", layout = EraLayout.class)
@PageTitle("Update doctor | ERA CRM")
public class DoctorUpdateView extends VerticalLayout {
    private final Converter converter;
    private final AdminService adminService;
    DoctorRequestUpdateDTO updateDTO;
    DoctorResponseDTO responseDTO;
    DoctorUpdateForm form;


    @Autowired
    public DoctorUpdateView(Converter converter, AdminService adminService){
        this.converter = converter;
        this.adminService = adminService;

        addClassNames("doctorUpdate-view");

        responseDTO = ComponentUtil.getData(getCurrent(), DoctorResponseDTO.class);

        if(responseDTO != null) {
            convertData();
            configureForm(updateDTO);

            add(form);
        }else{
            add(new H3("Вы не выбрали пациента для редактирования"));
        }
    }


    private void convertData() {
        updateDTO = converter.convertToDoctorRequestUpdateDTO(responseDTO);
    }


    private void configureForm(DoctorRequestUpdateDTO editDoctor) {
        form = new DoctorUpdateForm(editDoctor);

        form.setWidth("25em");
        form.addSaveListener(this::savePatient);
        form.addCloseListener(this::closeForm);
    }


    private void closeForm(DoctorUpdateForm.CloseEvent closeEvent) {
        form.setBinder(null);
        navigateToPatient(responseDTO);
    }


    private void savePatient(DoctorUpdateForm.SaveEvent event) {
        navigateToPatient(converter.convertToDoctorResponseDTO(
                adminService.updateDoctor(converter.convertToDoctor(event.getDoctorRequestUpdateDTO()))));
    }


    private void navigateToPatient(DoctorResponseDTO dto){
        ComponentUtil.setData(UI.getCurrent(), DoctorResponseDTO.class, dto);
        getUI().get().navigate("/admin/doctor");
    }
}
