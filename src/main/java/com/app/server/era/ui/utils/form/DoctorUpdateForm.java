package com.app.server.era.ui.utils.form;

import com.app.server.era.backend.dto.DoctorRequestUpdateDTO;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;

public class DoctorUpdateForm extends FormLayout {
    private TextField lastName = new TextField("Фамилия");
    private TextField firstName = new TextField("Имя");
    private TextField surName = new TextField("Отчество");
    Binder<DoctorRequestUpdateDTO> binder = new BeanValidationBinder<>(DoctorRequestUpdateDTO.class);
    Button save = new Button("Сохранить");
    Button close = new Button("Отмена");


    public DoctorUpdateForm(DoctorRequestUpdateDTO dto){
        addClassName("doctorUpdate-form");

        configureBinder(dto);

        add(lastName, firstName, surName, createButtonsLayout());
    }


    private void configureBinder(DoctorRequestUpdateDTO dto) {
        binder.bindInstanceFields(this);
        setBinder(dto);
    }


    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, close);
    }


    private void validateAndSave() {
        if(binder.isValid()) {
            fireEvent(new SaveEvent(this, binder.getBean()));
        }
    }


    public void setBinder(DoctorRequestUpdateDTO dto) {
        binder.setBean(dto);
    }


    public static abstract class DoctorUpdateFormEvent extends ComponentEvent<DoctorUpdateForm> {
        private DoctorRequestUpdateDTO dto;

        protected DoctorUpdateFormEvent(DoctorUpdateForm source, DoctorRequestUpdateDTO doctorDTO) {
            super(source, false);
            this.dto = doctorDTO;
        }

        public DoctorRequestUpdateDTO getDoctorRequestUpdateDTO() {
            return dto;
        }
    }


    public static class SaveEvent extends DoctorUpdateFormEvent {
        SaveEvent(DoctorUpdateForm source, DoctorRequestUpdateDTO dto) {
            super(source, dto);
        }
    }


    public static class CloseEvent extends DoctorUpdateFormEvent {
        CloseEvent(DoctorUpdateForm source) {
            super(source, null);
        }
    }


    public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
        return addListener(SaveEvent.class, listener);
    }


    public Registration addCloseListener(ComponentEventListener<CloseEvent> listener) {
        return addListener(CloseEvent.class, listener);
    }
}
