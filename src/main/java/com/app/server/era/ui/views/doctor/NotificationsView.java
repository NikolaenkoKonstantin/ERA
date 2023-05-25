package com.app.server.era.ui.views.doctor;

import com.app.server.era.backend.dto.NotificationResponseDTO;
import com.app.server.era.backend.dto.PatientResponseDTO;
import com.app.server.era.backend.models.User;
import com.app.server.era.backend.security.UserDetailsImpl;
import com.app.server.era.backend.services.DoctorService;
import com.app.server.era.backend.utils.Converter;
import com.app.server.era.ui.utils.layout.EraLayout;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

//Представление уведомлений врача
@RolesAllowed("ROLE_DOCTOR")
@Route(value = "/doctor/notification", layout = EraLayout.class)
@PageTitle("Notification | ERA CRM")
public class NotificationsView extends VerticalLayout {
    private final Converter converter;
    private final DoctorService doctorService;
    Grid<NotificationResponseDTO> grid = new Grid<>(NotificationResponseDTO.class);


    //Конструктор
    @Autowired
    public NotificationsView(Converter converter, DoctorService doctorService){
        this.converter = converter;
        this.doctorService = doctorService;

        addClassName("notifications-view");

        setSizeFull();
        configureGrid();

        add(new H3("Уведомления"), grid);
    }


    //Конфигурация сетки
    private void configureGrid() {
        configureColumns();

        grid.addClassNames("notification-grid");
        grid.setSizeFull();
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.setItems(loadGrid());
        grid.asSingleSelect().addValueChangeListener(event -> deleteNotification(event.getValue()));
    }


    //Удаление уведомления
    private void deleteNotification(NotificationResponseDTO dto){
        //возможно нужно сделать удаление всё таки в другом месте TODO
        doctorService.deleteNotification(dto.getId());
        navigateToPatient(dto);
    }


    //Конфигурация колонок сетки
    private void configureColumns() {
        grid.removeAllColumns();
        grid.addColumn(NotificationResponseDTO::getCardNumber).setHeader("Номер карты");
        grid.addColumn(new LocalDateTimeRenderer<>(
                        NotificationResponseDTO::getDateTime,
                        "dd/MM/yy")).setHeader("Дата");
    }


    //Загрузка данных сетки
    private List<NotificationResponseDTO> loadGrid() {
        User userDoctor = ((UserDetailsImpl) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal()).getUser();

        return doctorService.findAllNotificationByDoctor(doctorService.findDoctorByUser(userDoctor))
                .stream()
                .map(converter::convertToNotificationResponseDTO)
                .toList();
    }


    //Переход на страницу пациента
    private void navigateToPatient(NotificationResponseDTO dto){
        ComponentUtil.setData(UI.getCurrent(), PatientResponseDTO.class,
                converter.convertToResponsePatientDTO(
                        doctorService.findPatientByCardNumber(dto.getCardNumber())));

        getUI().get().navigate(PatientView.class);
    }
}
