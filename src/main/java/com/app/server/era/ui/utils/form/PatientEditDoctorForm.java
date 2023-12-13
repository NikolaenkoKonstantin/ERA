package com.app.server.era.ui.utils.form;

import com.app.server.era.backend.dto.PatientEditDoctorRequest;
import com.app.server.era.backend.models.Doctor;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;

import java.util.List;

public class PatientEditDoctorForm extends FormLayout {
    ComboBox<Doctor> doctor = new ComboBox<>("Доктор");
    Binder<PatientEditDoctorRequest> binder = new BeanValidationBinder<>(PatientEditDoctorRequest.class);
    Button save = new Button("Сохранить");
    Button close = new Button("Отмена");


    public PatientEditDoctorForm(PatientEditDoctorRequest request, List<Doctor> doctors){
        addClassName("patientEditDoctor-form");

        configureBinder(request);
        configureComboBox(doctors);

        add(doctor, createButtonsLayout());
    }


    private void configureComboBox(List<Doctor> doctors) {
        doctor.setItems(doctors);
        doctor.setItemLabelGenerator(el -> el.getLastName() + el.getFirstName() + el.getSurName());
    }


    private void configureBinder(PatientEditDoctorRequest request) {
        binder.bindInstanceFields(this);
        setBinder(request);
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


    public void setBinder(PatientEditDoctorRequest dto) {
        binder.setBean(dto);
    }


    public static abstract class PatientEditDoctorFormEvent extends ComponentEvent<PatientEditDoctorForm> {
        private PatientEditDoctorRequest dto;

        protected PatientEditDoctorFormEvent(PatientEditDoctorForm source, PatientEditDoctorRequest dto) {
            super(source, false);
            this.dto = dto;
        }

        public PatientEditDoctorRequest getPatientEditDoctorRequest() {
            return dto;
        }
    }


    public static class SaveEvent extends PatientEditDoctorFormEvent {
        SaveEvent(PatientEditDoctorForm source, PatientEditDoctorRequest dto) {
            super(source, dto);
        }
    }


    public static class CloseEvent extends PatientEditDoctorFormEvent {
        CloseEvent(PatientEditDoctorForm source) {
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
