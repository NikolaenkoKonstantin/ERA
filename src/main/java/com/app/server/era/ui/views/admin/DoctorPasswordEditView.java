package com.app.server.era.ui.views.admin;

import com.app.server.era.backend.dto.DoctorResponseDTO;
import com.app.server.era.backend.dto.PasswordEditRequest;
import com.app.server.era.backend.services.AdminService;
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

//представление измерения пароля врача
@RolesAllowed("ROLE_ADMIN")
@Route(value = "/admin/password", layout = EraLayout.class)
@PageTitle("Edit password | ERA CRM")
public class DoctorPasswordEditView extends VerticalLayout {
    private final Converter converter;
    private final AdminService adminService;
    PasswordEditForm form;
    DoctorResponseDTO responseDTO;
    PasswordEditRequest editPassword;


    //Конструктор
    @Autowired
    public DoctorPasswordEditView(Converter converter, AdminService adminService){
        this.converter = converter;
        this.adminService = adminService;

        responseDTO = ComponentUtil.getData(getCurrent(), DoctorResponseDTO.class);

        if(responseDTO != null) {
            convertData();

            addClassName("doctorPasswordEdit-view");
            configureForm(editPassword);

            add(form);
        }else{
            add(new H3("Не выбран пациент для изменения пароля"));
        }
    }


    //Преобразование в нужные данные (вызор конвертора)
    private void convertData() {
        editPassword = converter.convertToPasswordEditRequest(responseDTO);
    }


    //Конфигурация формы
    private void configureForm(PasswordEditRequest request) {
        form = new PasswordEditForm(request);

        form.setWidth("25em");
        form.addSaveListener(this::savePassword);
        form.addCloseListener(event -> navigateToDoctor());
    }


    //Сохрание нового пароля
    private void savePassword(PasswordEditForm.SaveEvent event) {
        adminService.EditPassword(event.getPasswordEditRequest());
        navigateToDoctor();
    }


    //Переход на страницу врача
    private void navigateToDoctor(){
        ComponentUtil.setData(UI.getCurrent(), DoctorResponseDTO.class, responseDTO);
        getUI().get().navigate(DoctorView.class);
    }
}
