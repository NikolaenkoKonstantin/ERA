package com.app.server.era.ui.views.doctor;

import com.app.server.era.backend.dto.PatientResponseDTO;
import com.app.server.era.backend.models.User;
import com.app.server.era.backend.security.UserDetailsImpl;
import com.app.server.era.backend.services.DoctorService;
import com.app.server.era.backend.utils.Converter;
import com.app.server.era.ui.utils.layout.EraLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.*;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
@RolesAllowed("ROLE_DOCTOR")
@Route(value = "/doctor/patients", layout = EraLayout.class)
@PageTitle("Patients | ERA CRM")
public class PatientsView extends VerticalLayout {
    private final Converter converter;
    private final DoctorService doctorService;
    Grid<PatientResponseDTO> grid = new Grid<>(PatientResponseDTO.class);
    TextField filterText = new TextField();


    @Autowired
    public PatientsView(Converter converter, DoctorService doctorService){
        this.converter = converter;
        this.doctorService = doctorService;

        addClassName("patients-view");

        setSizeFull();
        configureGrid();

        add(getToolbar(), grid);
    }


    private Component getToolbar() {
        filterText.setPlaceholder("Фильтр...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> grid.setItems(loadGrid()));

        Button addContactButton = new Button("Добавить пациента");

        addContactButton.addClickListener(click -> getUI().get().navigate("/doctor/create"));

        var toolbar = new HorizontalLayout(filterText, addContactButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }


    private List<PatientResponseDTO> loadGrid(){
        User userDoctor = ((UserDetailsImpl) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal()).getUser();

        return doctorService.findAllPatientsByDoctor(filterText.getValue(), doctorService.findDoctorByUser(userDoctor))
                .stream()
                .map(converter::convertToResponsePatientDTO)
                .toList();
    }


    private void configureGrid() {
        configureColumns();

        grid.addClassNames("dimension-grid");
        grid.setSizeFull();
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.setItems(loadGrid());
        grid.asSingleSelect().addValueChangeListener(event -> navigateToPatient(event.getValue()));
    }


    private void configureColumns() {
        grid.removeAllColumns();
        grid.addColumn(PatientResponseDTO::getLastName).setHeader("Фамилия");
        grid.addColumn(PatientResponseDTO::getFirstName).setHeader("Имя");
        grid.addColumn(PatientResponseDTO::getSurName).setHeader("Отчество");
        grid.addColumn(PatientResponseDTO::getAge).setHeader("Возраст");
        grid.addColumn(PatientResponseDTO::getCardNumber).setHeader("Номер карты");
    }


    private void navigateToPatient(PatientResponseDTO dto){
        ComponentUtil.setData(UI.getCurrent(), PatientResponseDTO.class, dto);
        getUI().get().navigate("/doctor/patient");
    }

}
