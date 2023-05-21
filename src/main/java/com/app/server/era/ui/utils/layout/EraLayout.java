package com.app.server.era.ui.utils.layout;

import com.app.server.era.backend.models.User;
import com.app.server.era.backend.security.UserDetailsImpl;
import com.app.server.era.backend.services.SecurityService;
import com.app.server.era.ui.views.admin.DoctorCreateView;
import com.app.server.era.ui.views.admin.DoctorsView;
import com.app.server.era.ui.views.doctor.NotificationsView;
import com.app.server.era.ui.views.doctor.PatientCreateView;
import com.app.server.era.ui.views.doctor.PatientsView;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;


public class EraLayout extends AppLayout {
    private final SecurityService securityService;


    @Autowired
    public EraLayout(SecurityService securityService) {
        this.securityService = securityService;
        createHeader();
        createDrawer();
    }


    private void createHeader() {
        H1 logo = new H1("ERA | Vaadin CRM");
        logo.addClassNames(
                LumoUtility.FontSize.LARGE,
                LumoUtility.Margin.MEDIUM);

        Button logout = new Button("Log out", e -> securityService.logout());

        var header = new HorizontalLayout(new DrawerToggle(), logo, logout);

        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.expand(logo);
        header.setWidthFull();
        header.addClassNames(
                LumoUtility.Padding.Vertical.NONE,
                LumoUtility.Padding.Horizontal.MEDIUM);

        addToNavbar(header);

    }


    private void createDrawer() {
        User user = ((UserDetailsImpl) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal()).getUser();

        if(user.getRole().equals("ROLE_DOCTOR")) {
            addToDrawer(new VerticalLayout(
                    new RouterLink("Пациенты", PatientsView.class),
                    new RouterLink("Добавить пациента", PatientCreateView.class),
                    new RouterLink("Уведомления", NotificationsView.class)
            ));
        } else if(user.getRole().equals("ROLE_ADMIN")){
            addToDrawer(new VerticalLayout(
                    new RouterLink("Доктора", DoctorsView.class),
                    new RouterLink("Добавить доктора", DoctorCreateView.class)
            ));
        }
    }
}
