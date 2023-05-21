package com.app.server.era.ui.views.admin;

import com.app.server.era.backend.dto.DoctorResponseDTO;
import com.app.server.era.backend.dto.PatientResponseDTO;
import com.app.server.era.backend.services.AdminService;
import com.app.server.era.backend.services.DoctorService;
import com.app.server.era.backend.utils.Converter;
import com.app.server.era.ui.utils.layout.EraLayout;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.vaadin.flow.component.UI.getCurrent;

@RolesAllowed("ROLE_ADMIN")
@Route(value = "/admin/doctor", layout = EraLayout.class)
@PageTitle("Doctor | ERA CRM")
public class DoctorView extends VerticalLayout {
    private final DoctorService doctorService;
    private final Converter converter;
    private final AdminService adminService;
    DoctorResponseDTO dto;
    Grid<PatientResponseDTO> grid = new Grid<>(PatientResponseDTO.class);
    Button editDoctor = new Button("Редактировать");
    Button editPassword = new Button("Изменить пароль");
    Button block = new Button("заблокировать");
    Button unlock = new Button("Разблокировать");
    H3 active = new H3();
    TextField filterText = new TextField();


    @Autowired
    public DoctorView(DoctorService doctorService, Converter converter, AdminService adminService){
        this.doctorService = doctorService;
        this.converter = converter;
        this.adminService = adminService;

        addClassName("doctor-view");

        dto = ComponentUtil.getData(getCurrent(), DoctorResponseDTO.class);

        if(dto != null) {
            setSizeFull();
            configureGrid();
            configureButtons();
            configureFilter();

            add(configureDoctor(), new HorizontalLayout(editDoctor, editPassword, block, unlock), filterText, grid);
        }else{
            add(new H3("Вы не выбрали доктора, вернитесь на страницу \"doctors\" для его выбора"));
        }
    }

    private void configureFilter(){
        filterText.setPlaceholder("Фильтр...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> grid.setItems(loadGrid()));
    }


    private void configureButtons() {
        editDoctor.addClickListener(e -> navigateToUpdate());
        editPassword.addClickListener(event -> navigateToPassword());
        block.addClickListener(event -> blockDoctor());
        unlock.addClickListener(event -> unlockDoctor());
    }


    private List<PatientResponseDTO> loadGrid(){
        return doctorService.findAllPatientsByDoctor(filterText.getValue(), doctorService.findDoctorById(dto.getId()))
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


    private void blockDoctor() {
        adminService.blockDoctor(dto.getId());
        dto.setActive(false);
        active.setText("Статус аккаунта: " + (dto.isActive() ? "рабочий" : "заблокиванный"));
    }


    private void unlockDoctor(){
        adminService.unlockDoctor(dto.getId());
        dto.setActive(true);
        active.setText("Статус аккаунта: " + (dto.isActive() ? "рабочий" : "заблокиванный"));
    }


    private VerticalLayout configureDoctor(){
        H3 doctor = new H3("Доктор ");
        H3 lastName = new H3("Фамилия: " + dto.getLastName());
        H3 firstName = new H3("Имя: " + dto.getFirstName());
        H3 surName = new H3("Отчество: " + dto.getSurName());
        active.setText("Статус аккаунта: " + (dto.isActive() ? "рабочий" : "заблокиванный"));

        return new VerticalLayout(doctor, lastName, firstName, surName, active);
    }


    private void navigateToUpdate(){
        ComponentUtil.setData(UI.getCurrent(), DoctorResponseDTO.class, dto);
        getUI().get().navigate("/admin/update");
    }


    private void navigateToPassword(){
        ComponentUtil.setData(UI.getCurrent(), DoctorResponseDTO.class, dto);
        getUI().get().navigate("/admin/password");
    }
}
