package com.app.server.era.ui.views.doctor;

import com.app.server.era.backend.dto.PatientResponseDTO;
import com.app.server.era.backend.models.Dimension;
import com.app.server.era.backend.services.DoctorService;
import com.app.server.era.ui.utils.layout.EraLayout;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.charts.model.*;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import com.vaadin.flow.component.charts.Chart;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.vaadin.flow.component.UI.getCurrent;


@RolesAllowed("ROLE_DOCTOR")
@Route(value = "/doctor/rehabilitation", layout = EraLayout.class)
@PageTitle("rehabilitation | ERA CRM")
public class RehabilitationView extends VerticalLayout {
    private final DoctorService doctorService;
    final Chart chartAngle = new Chart();
    Chart chartCountBendAndState = new Chart();
    ComboBox<String> elbowKnee = new ComboBox<>("Сустав");
    ComboBox<String> leftRight = new ComboBox<>("Ориентация");
    Button search = new Button("Поиск");

    DataSeries dsFlexionAngle = new DataSeries();
    DataSeries dsExtensionAngle = new DataSeries();
    DataSeries dsCountBend = new DataSeries();
    DataSeries dsState = new DataSeries();

    List<Dimension> dtoCharts;
    PatientResponseDTO dto;


    @Autowired
    public RehabilitationView(DoctorService doctorService){
        this.doctorService = doctorService;
        addClassName("rehabilitation-view");

        dto = ComponentUtil.getData(getCurrent(), PatientResponseDTO.class);

        if(dto != null) {
            configureComboBox();
            configureDataSeries();
            configureChartAngle();
            configureChartCountBendAndState();
            configureButton();

            add(new H3("Реабилитация"), new HorizontalLayout(elbowKnee, leftRight, search),
                    chartAngle, chartCountBendAndState);
        }else{
            add(new H3("Не выбран пациент для отображения графика лечения"));
        }
    }


    private void configureButton(){
        search.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        search.addClickListener(event -> {
            if(!elbowKnee.isEmpty() && !leftRight.isEmpty()) {
                rebootCharts();
            }
        });
    }

    private void rebootCharts() {
        loadChart();
        if(!dtoCharts.isEmpty()) {
            updateData();

            chartCountBendAndState.drawChart();
            chartAngle.drawChart();

            chartCountBendAndState.setVisible(true);
            chartAngle.setVisible(true);
        } else {
            chartCountBendAndState.setVisible(false);
            chartAngle.setVisible(false);
        }
    }


    private void updateData(){
        Number[] numbersFlexionAngle = new Double[dtoCharts.size()];
        Number[] numbersExtensionAngle = new Double[dtoCharts.size()];
        Number[] numbersCountBend = new Integer[dtoCharts.size()];
        Number[] numbersState = new Integer[dtoCharts.size()];

        for (int i = 0; i < dtoCharts.size(); i++){
            numbersFlexionAngle[i] = dtoCharts.get(i).getFlexionAngle();
            numbersExtensionAngle[i] = dtoCharts.get(i).getExtensionAngle();
            numbersCountBend[i] = dtoCharts.get(i).getCountBend();
            numbersState[i] = dtoCharts.get(i).getState();
        }

        dsFlexionAngle.setData(numbersFlexionAngle);
        dsExtensionAngle.setData(numbersExtensionAngle);
        dsCountBend.setData(numbersCountBend);
        dsState.setData(numbersState);
    }


    private void configureComboBox(){
        elbowKnee.setItems(List.of("Локоть", "Колено"));
        leftRight.setItems(List.of("Лево", "Право"));
    }


    private void loadChart() {
        dtoCharts = doctorService.findAllForCharts(dto.getId(),
                elbowKnee.getValue().equals("Локоть") ? "elbow" : "knee",
                leftRight.getValue().equals("Лево") ? "left" : "right");
    }


    private void configureDataSeries(){
        dsFlexionAngle.setName("Угол сгибания");
        dsExtensionAngle.setName("Угол разгибания");
        dsCountBend.setName("Количество сгибаний");
        dsState.setName("Боль в суставе");
    }


    private void configureChartCountBendAndState() {
        Configuration configuration = chartCountBendAndState.getConfiguration();

        configureLegend(configuration, "Количество сгибаний/оценка боли в суставе",
                "Количество сгибаний/значение боли");

        configuration.addSeries(dsCountBend);
        configuration.addSeries(dsState);

        chartCountBendAndState.setVisible(false);
    }


    public void configureChartAngle(){
        Configuration configuration = chartAngle.getConfiguration();

        configureLegend(configuration, "Угол сгибания/разгибания", "Угол в градусах");

        configuration.addSeries(dsFlexionAngle);
        configuration.addSeries(dsExtensionAngle);

        chartAngle.setVisible(false);
    }


    private void configureLegend(Configuration configuration, String title, String titleY){
        configuration.setTitle(title);
        configuration.getyAxis().setTitle(titleY);

        Legend legend = configuration.getLegend();
        legend.setLayout(LayoutDirection.VERTICAL);
        legend.setVerticalAlign(VerticalAlign.MIDDLE);
        legend.setAlign(HorizontalAlign.RIGHT);

        PlotOptionsSeries plotOptionsSeries = new PlotOptionsSeries();
        plotOptionsSeries.setPointStart(1);
        configuration.setPlotOptions(plotOptionsSeries);
    }

}
