package com.app.server.era.ui.utils.form;

import com.app.server.era.backend.dto.MessageSendRequest;
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

//Форма отправки сообщения от врача пациенту
public class SendMessageEmailForm extends FormLayout {
    TextField message = new TextField("Сообщение");
    Button send = new Button("Отправить");
    Button close = new Button("Отмена");
    Binder<MessageSendRequest> binder = new BeanValidationBinder<>(MessageSendRequest.class);


    //Конструктор
    public SendMessageEmailForm(MessageSendRequest request) {
        addClassName("sendMessage-form");

        configureBinder(request);

        add(message, createButtonsLayout());
    }


    //Конфигурация валидатора
    private void configureBinder(MessageSendRequest request) {
        binder.bindInstanceFields(this);
        setBinder(request);
    }


    //Конфигурация кнопок и создание компонента
    private Component createButtonsLayout() {
        send.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        send.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        send.addClickListener(event -> validateAndSave());
        close.addClickListener(event -> fireEvent(new CloseEvent(this, binder.getBean())));

        binder.addStatusChangeListener(e -> send.setEnabled(binder.isValid()));
        return new HorizontalLayout(send, close);
    }


    //Проверка валидации
    private void validateAndSave() {
        if(binder.isValid()) {
            fireEvent(new SendEvent(this, binder.getBean()));
        }
    }


    //Установка значения валидатору
    public void setBinder(MessageSendRequest dto) {
        binder.setBean(dto);
    }


    public static abstract class SendMessageEmailFormEvent extends ComponentEvent<SendMessageEmailForm> {
        private MessageSendRequest dto;

        protected SendMessageEmailFormEvent(SendMessageEmailForm source, MessageSendRequest dto) {
            super(source, false);
            this.dto = dto;
        }

        public MessageSendRequest getMessageSendRequest() {
            return dto;
        }
    }


    public static class SendEvent extends SendMessageEmailFormEvent {
        SendEvent(SendMessageEmailForm source, MessageSendRequest dto) {
            super(source, dto);
        }
    }


    public static class CloseEvent extends SendMessageEmailFormEvent {
        CloseEvent(SendMessageEmailForm source, MessageSendRequest dto) {
            super(source, dto);
        }
    }


    public Registration addSaveListener(ComponentEventListener<SendEvent> listener) {
        return addListener(SendEvent.class, listener);
    }


    public Registration addCloseListener(ComponentEventListener<CloseEvent> listener) {
        return addListener(CloseEvent.class, listener);
    }
}
