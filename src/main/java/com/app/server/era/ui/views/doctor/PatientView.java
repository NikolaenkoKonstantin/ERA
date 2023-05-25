package com.app.server.era.ui.views.doctor;

import com.app.server.era.backend.dto.CloseTreatmentRequest;
import com.app.server.era.backend.dto.DimensionResponseDTO;
import com.app.server.era.backend.dto.PatientResponseDTO;
import com.app.server.era.backend.dto.ScheduleRequest;
import com.app.server.era.backend.models.User;
import com.app.server.era.backend.security.UserDetailsImpl;
import com.app.server.era.backend.services.DoctorService;
import com.app.server.era.backend.utils.Converter;
import com.app.server.era.ui.utils.layout.EraLayout;
import com.app.server.era.ui.views.admin.PatientEditDoctorView;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import com.vaadin.flow.router.*;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static com.vaadin.flow.component.UI.getCurrent;

//Представление пациента
@RolesAllowed({"ROLE_DOCTOR", "ROLE_ADMIN"})
@Route(value = "/doctor/patient", layout = EraLayout.class)
@PageTitle("Patient | ERA CRM")
public class PatientView extends VerticalLayout{
    private final DoctorService doctorService;
    private final Converter converter;
    PatientResponseDTO dto;
    Grid<DimensionResponseDTO> grid = new Grid<>(DimensionResponseDTO.class);
    Button editPatient = new Button("Редактировать");
    Button editPassword = new Button("Изменить пароль");
    Button closeTreatment = new Button("Закрыть лечение");
    Button sendMessage = new Button("Отправить сообщение");
    Button editDoctor = new Button("Изменить доктора");
    List<DimensionResponseDTO> listDim;


    //Конструктор
    @Autowired
    public PatientView(DoctorService webService, Converter converter){
        this.doctorService = webService;
        this.converter = converter;

        addClassName("patient-view");
        dto = ComponentUtil.getData(getCurrent(), PatientResponseDTO.class);

        if(dto != null) {
            setSizeFull();
            configureGrid();

            add(configurePatient(dto), configureButton(), grid);
        }else{
            add(new H3("Вы не выбрали пациента, вернитесь на страницу \"Patients\" для его выбора"));
        }

    }


    //Конфигурация кнопок
    private Component configureButton() {
        User user = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        if(user.getRole().equals("ROLE_DOCTOR")) {
            editPatient.addClickListener(this::navigateToUpdate);
            editPassword.addClickListener(this::navigateToPasswordEdit);
            closeTreatment.addClickListener(this::navigateToClosingTreatment);
            sendMessage.addClickListener(this::sendMessage);
            return new HorizontalLayout(editPatient, closeTreatment, editPassword, sendMessage);
        } else {
            editDoctor.addClickListener(this::navigateToEditDoctor);
            return new HorizontalLayout(editDoctor);
        }
    }


    //Переход на страницу назначения нового врача
    private void navigateToEditDoctor(ClickEvent<Button> buttonClickEvent) {
        ComponentUtil.setData(UI.getCurrent(), PatientResponseDTO.class, dto);
        getUI().get().navigate(PatientEditDoctorView.class);
    }


    //Переход на страницу отправки сообщения на почту
    private void sendMessage(ClickEvent<Button> buttonClickEvent) {
        ComponentUtil.setData(UI.getCurrent(), PatientResponseDTO.class, dto);
        getUI().get().navigate(SendMessageEmailView.class);
    }


    //Переход на страницу изменения пароля
    private void navigateToPasswordEdit(ClickEvent<Button> buttonClickEvent) {
        ComponentUtil.setData(UI.getCurrent(), PatientResponseDTO.class, dto);
        getUI().get().navigate(PatientPasswordEditView.class);
    }


    //Переход на страницу закрытия лечения
    private void navigateToClosingTreatment(ClickEvent<Button> buttonClickEvent) {
        ComponentUtil.setData(UI.getCurrent(), CloseTreatmentRequest.class,
                converter.convertToClosingTreatmentRequest(dto));

        getUI().get().navigate(ClosingTreatmentView.class);
    }


    //Конфигурация сетки
    private void configureGrid() {
        configureColumns();

        grid.addClassNames("dimension-grid");
        grid.setSizeFull();
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        listDim = loadGrid();
        grid.setItems(listDim);

        User user = ((UserDetailsImpl) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal()).getUser();
        if(user.getRole().equals("ROLE_DOCTOR")) {
            grid.asSingleSelect().addValueChangeListener(e -> navigateToRehabitation(e.getValue()));
        }
    }


    //Переход на страницу реабилитации
    private void navigateToRehabitation(DimensionResponseDTO dimDTO){
        ComponentUtil.setData(UI.getCurrent(), PatientResponseDTO.class, dto);
        ComponentUtil.setData(UI.getCurrent(), ScheduleRequest.class, converter.convertToScheduleRequest(dimDTO));
        getUI().get().navigate(RehabilitationView.class);
    }


    //Конфигурация колонок сетки
    private void configureColumns() {
        grid.removeAllColumns();
        grid.addColumn(DimensionResponseDTO::getLeftRight).setHeader("Ориентация");
        grid.addColumn(DimensionResponseDTO::getElbowKnee).setHeader("Сустав");
        grid.addColumn(DimensionResponseDTO::getFlexionAngle).setHeader("Угол сгибания");
        grid.addColumn(DimensionResponseDTO::getExtensionAngle).setHeader("Угол разгибания");
        grid.addColumn(DimensionResponseDTO::getCountBend).setHeader("Количество сгибаний");
        grid.addColumn(DimensionResponseDTO::getState).setHeader("Боль в суставе(1-5)");
        grid.addColumn(DimensionResponseDTO::getDistance).setHeader("Расстояние(м)");
        grid.addColumn(DimensionResponseDTO::getStatus).setHeader("Статус");
        grid.addColumn(new LocalDateTimeRenderer<>(
                DimensionResponseDTO::getDateTime,
                "dd/MM/yy"))
                .setHeader("Дата");
    }


    //Загрузка данных сетки
    private List<DimensionResponseDTO> loadGrid() {
        return doctorService.findAllDimensionByPatient(dto.getId())
                .stream()
                .map(converter::convertToResponseDimensionDTO)
                .toList();
    }


    //Конфигурация вывода текста
    private VerticalLayout configurePatient(PatientResponseDTO dto){
        H3 patient = new H3("Пациент: " + dto.getLastName() + " " + dto.getFirstName() + " " + dto.getSurName());
        H3 age = new H3("Возраст: " + dto.getAge());
        H3 cardNumber = new H3("Номер карты: " + dto.getCardNumber());

        return new VerticalLayout(patient, age, cardNumber);
    }


    //Переход на страницу редактирования пациента
    private void navigateToUpdate(ClickEvent<Button> buttonClickEvent){
        ComponentUtil.setData(UI.getCurrent(), PatientResponseDTO.class, dto);
        getUI().get().navigate(PatientUpdateView.class);
    }
}
