package com.app.server.era.ui.utils.form;

import com.app.server.era.backend.dto.CloseTreatmentRequest;
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


public class ClosingTreatmentForm extends FormLayout {
    ComboBox<String> elbowKnee = new ComboBox<>("Сустав");
    ComboBox<String> leftRight = new ComboBox<>("Ориентация");
    Button closeTreatment = new Button("Закрыть лечение");
    Button close = new Button("Отмена");
    Binder<CloseTreatmentRequest> binder = new BeanValidationBinder<>(CloseTreatmentRequest.class);

    public ClosingTreatmentForm() {
        addClassName("closingTreatment-form");

        configureComboBox();
        configureBinder();

        add(elbowKnee, leftRight, createButtonsLayout());
    }


    private void configureBinder(){
        binder.bindInstanceFields(this);
        setBinder(new CloseTreatmentRequest());
    }


    private void configureComboBox(){
        elbowKnee.setItems(List.of("Локтевой", "Коленный"));
        leftRight.setItems(List.of("Левая", "Правая"));
    }


    private Component createButtonsLayout() {
        closeTreatment.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        closeTreatment.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        closeTreatment.addClickListener(event -> validateAndSave());
        close.addClickListener(event -> fireEvent(
                new ClosingTreatmentForm.CloseEvent(this, binder.getBean())));

        binder.addStatusChangeListener(e -> closeTreatment.setEnabled(binder.isValid()));
        return new HorizontalLayout(closeTreatment, close);
    }


    private void validateAndSave() {
        if(binder.isValid()) {
            fireEvent(new ClosingTreatmentForm.CloseTreatmentEvent(this, binder.getBean()));
        }
    }


    public void setBinder(CloseTreatmentRequest dto) {
        binder.setBean(dto);
    }


    public static abstract class CloseTreatmentFormEvent extends ComponentEvent<ClosingTreatmentForm> {
        private CloseTreatmentRequest dto;

        protected CloseTreatmentFormEvent(ClosingTreatmentForm source, CloseTreatmentRequest dto) {
            super(source, false);
            this.dto = dto;
        }

        public CloseTreatmentRequest getCloseTreatmentRequest() {
            return dto;
        }
    }


    public static class CloseTreatmentEvent extends CloseTreatmentFormEvent {
        CloseTreatmentEvent(ClosingTreatmentForm source, CloseTreatmentRequest dto) {
            super(source, dto);
        }
    }


    public static class CloseEvent extends CloseTreatmentFormEvent {
        CloseEvent(ClosingTreatmentForm source, CloseTreatmentRequest dto) {
            super(source, dto);
        }
    }


    public Registration addCloseTreatmentListener(ComponentEventListener<CloseTreatmentEvent> listener) {
        return addListener(CloseTreatmentEvent.class, listener);
    }


    public Registration addCloseListener(ComponentEventListener<CloseEvent> listener) {
        return addListener(CloseEvent.class, listener);
    }
}
