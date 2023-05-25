package com.app.server.era.ui.utils.form;

import com.app.server.era.backend.dto.PatientRequestUpdateDTO;
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

//Форма редактирования пациента
public class PatientUpdateForm extends FormLayout {
    TextField lastName = new TextField("Lastname");
    TextField firstName = new TextField("Firstname");
    TextField surName = new TextField("Surname");
    TextField age = new TextField("Age");
    TextField cardNumber = new TextField("Card number");
    Button save = new Button("Сохранить");
    Button close = new Button("Отмена");
    Binder<PatientRequestUpdateDTO> binder = new BeanValidationBinder<>(PatientRequestUpdateDTO.class);


    //Конструктор
    public PatientUpdateForm(PatientRequestUpdateDTO editPatient) {
        addClassName("patientUpdate-form");

       configureBinder(editPatient);

        add(lastName, firstName, surName, age, cardNumber, createButtonsLayout());
    }


    //Конфигурация валидатора
    private void configureBinder(PatientRequestUpdateDTO editPatient) {
        binder.bindInstanceFields(this);
        setBinder(editPatient);
    }


    //Конфигурация кнопок и создание компонента
    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        close.addClickListener(event -> fireEvent(new CloseEvent(this, binder.getBean())));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, close);
    }


    //Проверка валидации
    private void validateAndSave() {
        if(binder.isValid()) {
            fireEvent(new PatientUpdateForm.SaveEvent(this, binder.getBean()));
        }
    }


    //Установка значения валидатору
    public void setBinder(PatientRequestUpdateDTO dto) {
        binder.setBean(dto);
    }


    public static abstract class PatientUpdateFormEvent extends ComponentEvent<PatientUpdateForm> {
        private PatientRequestUpdateDTO dto;

        protected PatientUpdateFormEvent(PatientUpdateForm source, PatientRequestUpdateDTO dto) {
            super(source, false);
            this.dto = dto;
        }

        public PatientRequestUpdateDTO getPatientRequestUpdateDTO() {
            return dto;
        }
    }


    public static class SaveEvent extends PatientUpdateFormEvent {
        SaveEvent(PatientUpdateForm source, PatientRequestUpdateDTO dto) {
            super(source, dto);
        }
    }


    public static class CloseEvent extends PatientUpdateFormEvent {
        CloseEvent(PatientUpdateForm source, PatientRequestUpdateDTO dto) {
            super(source, dto);
        }
    }


    public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
        return addListener(SaveEvent.class, listener);
    }


    public Registration addCloseListener(ComponentEventListener<CloseEvent> listener) {
        return addListener(CloseEvent.class, listener);
    }
}
