package com.app.server.era.ui.utils.form;


import com.app.server.era.backend.dto.DoctorRequestCreateDTO;
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

//Форма создания врача
public class DoctorCreateForm extends FormLayout {
    private TextField lastName = new TextField("Фамилия");
    private TextField firstName = new TextField("Имя");
    private TextField surName = new TextField("Отчество");
    private TextField login = new TextField("Логин");
    private TextField password = new TextField("Пароль");
    Binder<DoctorRequestCreateDTO> binder = new BeanValidationBinder<>(DoctorRequestCreateDTO.class);
    Button save = new Button("Сохранить");
    Button close = new Button("Отмена");


    //Конструктор
    public DoctorCreateForm(){
        addClassName("doctorCreate-form");

        configureBinder();

        add(lastName, firstName, surName, login, password, createButtonsLayout());
    }


    //Конфигурация валидатора
    private void configureBinder(){
        binder.bindInstanceFields(this);
        setBinder(new DoctorRequestCreateDTO());
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
    public void setBinder(DoctorRequestCreateDTO dto) {
        binder.setBean(dto);
    }


    public static abstract class DoctorCreateFormEvent extends ComponentEvent<DoctorCreateForm> {
        private DoctorRequestCreateDTO dto;

        protected DoctorCreateFormEvent(DoctorCreateForm source, DoctorRequestCreateDTO doctorDTO) {
            super(source, false);
            this.dto = doctorDTO;
        }

        public DoctorRequestCreateDTO getDoctorRequestDTO() {
            return dto;
        }
    }


    public static class SaveEvent extends DoctorCreateFormEvent {
        SaveEvent(DoctorCreateForm source, DoctorRequestCreateDTO dto) {
            super(source, dto);
        }
    }


    public static class CloseEvent extends DoctorCreateFormEvent {
        CloseEvent(DoctorCreateForm source) {
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
