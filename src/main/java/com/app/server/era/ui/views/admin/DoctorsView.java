package com.app.server.era.ui.views.admin;

import com.app.server.era.backend.dto.DoctorResponseDTO;
import com.app.server.era.backend.services.AdminService;
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
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@RolesAllowed("ROLE_ADMIN")
@Route(value = "/admin/doctors", layout = EraLayout.class)
@PageTitle("Doctors | ERA CRM")
public class DoctorsView extends VerticalLayout {
    private final Converter converter;
    private final AdminService adminService;
    Grid<DoctorResponseDTO> grid = new Grid<>(DoctorResponseDTO.class);
    TextField filterText = new TextField();


    @Autowired
    public DoctorsView(Converter converter, AdminService adminService){
        this.converter = converter;
        this.adminService = adminService;

        addClassName("doctors-view");

        setSizeFull();
        configureGrid();

        add(getToolbar(), grid);
    }


    private Component getToolbar() {
        filterText.setPlaceholder("Фильтр...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> grid.setItems(loadGrid()));

        Button addDoctorButton = new Button("Добавить доктора");

        addDoctorButton.addClickListener(click -> getUI().get().navigate("/admin/create"));

        var toolbar = new HorizontalLayout(filterText, addDoctorButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }


    private void configureGrid() {
        grid.addClassNames("doctor-grid");
        grid.setSizeFull();
        configureColumns();
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.setItems(loadGrid());
        grid.asSingleSelect().addValueChangeListener(event -> navigateToDoctor(event.getValue()));
    }


    private void configureColumns() {
        grid.removeAllColumns();
        grid.addColumn(DoctorResponseDTO::getLastName).setHeader("Фамилия");
        grid.addColumn(DoctorResponseDTO::getFirstName).setHeader("Имя");
        grid.addColumn(DoctorResponseDTO::getSurName).setHeader("Отчество");
        grid.addColumn(e -> e.isActive() ? "Рабочий" : "Заблокированный").setHeader("Статус аккаунта");
    }


    private void navigateToDoctor(DoctorResponseDTO dto) {
        ComponentUtil.setData(UI.getCurrent(), DoctorResponseDTO.class, dto);
        getUI().get().navigate("/admin/doctor");
    }


    private List<DoctorResponseDTO> loadGrid(){
        return adminService.findAllDoctorsByLastName(filterText.getValue())
                .stream()
                .map(converter::convertToDoctorResponseDTO)
                .toList();
    }
}
