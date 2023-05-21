package com.app.server.era.ui.views.admin;

import com.app.server.era.backend.dto.DoctorRequestCreateDTO;
import com.app.server.era.backend.dto.DoctorResponseDTO;
import com.app.server.era.backend.services.AdminService;
import com.app.server.era.backend.services.RegistrationService;
import com.app.server.era.backend.utils.Converter;
import com.app.server.era.ui.utils.form.DoctorCreateForm;
import com.app.server.era.ui.utils.layout.EraLayout;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

@RolesAllowed("ROLE_ADMIN")
@Route(value = "/admin/create", layout = EraLayout.class)
@PageTitle("Create doctor | ERA CRM")
public class DoctorCreateView extends VerticalLayout {
    private final RegistrationService regService;
    private final Converter converter;
    private final AdminService adminService;
    DoctorCreateForm form;


    @Autowired
    public DoctorCreateView(RegistrationService regService, Converter converter, AdminService adminService) {
        this.regService = regService;
        this.converter = converter;
        this.adminService = adminService;

        addClassName("doctorCreate-view");
        configureForm();

        add(form);
    }


    private void configureForm() {
        form = new DoctorCreateForm();

        form.setWidth("25em");
        form.addSaveListener(this::saveDoctor);
        form.addCloseListener(event -> navigateToDoctors());
    }


    private void navigateToDoctors() {
        form.setBinder(null);
        getUI().get().navigate("/admin/doctors");
    }


    private void saveDoctor(DoctorCreateForm.SaveEvent event) {
        DoctorRequestCreateDTO dto = event.getDoctorRequestDTO();

        navigateToDoctor(converter.convertToDoctorResponseDTO(adminService.createDoctor(
                converter.convertToDoctor(dto), regService.registrateDoctor(converter.convertToUser(dto)))));
    }


    private void navigateToDoctor(DoctorResponseDTO dto){
        ComponentUtil.setData(UI.getCurrent(), DoctorResponseDTO.class, dto);
        getUI().get().navigate("/admin/doctor");
    }
}
