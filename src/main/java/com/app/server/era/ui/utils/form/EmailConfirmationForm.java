package com.app.server.era.ui.utils.form;

import com.app.server.era.backend.dto.EmailCodeRequest;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;

public class EmailConfirmationForm extends FormLayout {
    TextField code = new TextField("Код");
    Binder<EmailCodeRequest> binder = new Binder<>(EmailCodeRequest.class);
    Button send = new Button("Отправить");
    Button close = new Button("Закрыть");


    public EmailConfirmationForm(){
        addClassName("emailConfirmation-form");

        code.setMaxLength(4);
        configureBinder();
        createButtonsLayout();

        add(code, new HorizontalLayout(send, close));
    }


    private Component createButtonsLayout() {
        send.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        send.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        send.addClickListener(event -> fireEvent(new SendEvent(this, binder.getBean())));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        //binder.addStatusChangeListener(e -> send.setEnabled(binder.isValid()));
        return new HorizontalLayout(send, close);
    }


    private void configureBinder() {
        binder.bindInstanceFields(this);
        setBinder(new EmailCodeRequest());
    }


    public void setBinder(EmailCodeRequest dto) {
        binder.setBean(dto);
    }



    public static abstract class EmailConfirmationFormEvent extends ComponentEvent<EmailConfirmationForm> {
        private EmailCodeRequest dto;

        protected EmailConfirmationFormEvent(EmailConfirmationForm source, EmailCodeRequest doctorDTO) {
            super(source, false);
            this.dto = doctorDTO;
        }

        public EmailCodeRequest getEmailCodeRequest() {
            return dto;
        }
    }


    public static class SendEvent extends EmailConfirmationFormEvent {
        SendEvent(EmailConfirmationForm source, EmailCodeRequest dto) {
            super(source, dto);
        }
    }


    public static class CloseEvent extends EmailConfirmationFormEvent {
        CloseEvent(EmailConfirmationForm source) {
            super(source, null);
        }
    }


    public Registration addSendListener(ComponentEventListener<SendEvent> listener) {
        return addListener(SendEvent.class, listener);
    }


    public Registration addCloseListener(ComponentEventListener<CloseEvent> listener) {
        return addListener(CloseEvent.class, listener);
    }
}
