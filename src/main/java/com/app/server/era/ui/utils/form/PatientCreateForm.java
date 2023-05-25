package com.app.server.era.ui.utils.form;

import com.app.server.era.backend.dto.PatientRequestCreateDTO;
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

//Форма создания пациента
public class PatientCreateForm extends FormLayout {
    TextField lastName = new TextField("Фамилия");
    TextField firstName = new TextField("Имя");
    TextField surName = new TextField("Отчество");
    TextField age = new TextField("Возраст");
    TextField cardNumber = new TextField("Номер карты");
    TextField email = new TextField("Электронная почта");
    TextField password = new TextField("Пароль");
    Button save = new Button("Сохранить");
    Button close = new Button("Отмена");
    Binder<PatientRequestCreateDTO> binder = new BeanValidationBinder<>(PatientRequestCreateDTO.class);


    //Конструктор
    public PatientCreateForm() {
        addClassName("patientCreate-form");

        configureBinder();

        add(lastName, firstName, surName, age, cardNumber, email, password, createButtonsLayout());
    }


    //Конфигурация валидатора
    private void configureBinder() {
        binder.bindInstanceFields(this);
        setBinder(new PatientRequestCreateDTO());
    }


    //Конфигурация кнопок и создание компонента
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


    //Проверка валидации
    private void validateAndSave() {
        if(binder.isValid()) {
            fireEvent(new SaveEvent(this, binder.getBean()));
        }
    }


    //Установка значения валидатору
    public void setBinder(PatientRequestCreateDTO dto) {
        binder.setBean(dto);
    }


    public static abstract class PatientCreateFormEvent extends ComponentEvent<PatientCreateForm> {
        private PatientRequestCreateDTO dto;

        protected PatientCreateFormEvent(PatientCreateForm source, PatientRequestCreateDTO dto) {
            super(source, false);
            this.dto = dto;
        }

        public PatientRequestCreateDTO getRequestPatientDTO() {
            return dto;
        }
    }


    public static class SaveEvent extends PatientCreateFormEvent {
        SaveEvent(PatientCreateForm source, PatientRequestCreateDTO dto) {
            super(source, dto);
        }
    }


    public static class CloseEvent extends PatientCreateFormEvent {
        CloseEvent(PatientCreateForm source) {
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
