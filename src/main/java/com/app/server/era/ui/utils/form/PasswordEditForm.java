package com.app.server.era.ui.utils.form;

import com.app.server.era.backend.dto.PasswordEditRequest;
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

//Форма Изменения пароля
public class PasswordEditForm extends FormLayout {
    private TextField password = new TextField("Пароль");
    Binder<PasswordEditRequest> binder = new BeanValidationBinder<>(PasswordEditRequest.class);
    Button save = new Button("Сохранить");
    Button close = new Button("Отмена");


    //Конструктор
    public PasswordEditForm(PasswordEditRequest request){
        addClassName("passwordEdit-form");

        configureBinder(request);

        add(password, createButtonsLayout());
    }


    //Конфигурация валидатора
    private void configureBinder(PasswordEditRequest request) {
        binder.bindInstanceFields(this);
        setBinder(request);
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
    public void setBinder(PasswordEditRequest dto) {
        binder.setBean(dto);
    }


    public static abstract class PasswordEditFormEvent extends ComponentEvent<PasswordEditForm> {
        private PasswordEditRequest dto;

        protected PasswordEditFormEvent(PasswordEditForm source, PasswordEditRequest dto) {
            super(source, false);
            this.dto = dto;
        }

        public PasswordEditRequest getPasswordEditRequest() {
            return dto;
        }
    }


    public static class SaveEvent extends PasswordEditFormEvent {
        SaveEvent(PasswordEditForm source, PasswordEditRequest dto) {
            super(source, dto);
        }
    }


    public static class CloseEvent extends PasswordEditFormEvent {
        CloseEvent(PasswordEditForm source) {
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
