package com.app.server.era.ui.views.doctor;

import com.app.server.era.backend.dto.PatientResponseDTO;
import com.app.server.era.backend.dto.ScheduleRequest;
import com.app.server.era.backend.models.Dimension;
import com.app.server.era.backend.services.DoctorService;
import com.app.server.era.ui.utils.layout.EraLayout;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
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

//Предсталение графиков реабилитации
@RolesAllowed("ROLE_DOCTOR")
@Route(value = "/doctor/rehabilitation", layout = EraLayout.class)
@PageTitle("rehabilitation | ERA CRM")
public class RehabilitationView extends VerticalLayout {
    private final DoctorService doctorService;
    Chart chartAngle = new Chart();
    Chart chartCountBendAndState = new Chart();
    Button close = new Button("Вернуться к пациенту");

    //Данные графиков
    DataSeries dsFlexionAngle = new DataSeries();
    DataSeries dsExtensionAngle = new DataSeries();
    DataSeries dsCountBend = new DataSeries();
    DataSeries dsState = new DataSeries();

    //Данные для работы
    List<Dimension> dtoCharts;
    PatientResponseDTO patDTO;
    ScheduleRequest scheduleRequest;


    //Конструктор
    @Autowired
    public RehabilitationView(DoctorService doctorService){
        this.doctorService = doctorService;
        addClassName("rehabilitation-view");

        patDTO = ComponentUtil.getData(getCurrent(),
                PatientResponseDTO.class);
        scheduleRequest = ComponentUtil.getData(getCurrent(),
                ScheduleRequest.class);

        System.out.println(patDTO);
        System.out.println(scheduleRequest);

        if(patDTO != null && scheduleRequest != null) {
            configureDataSeries();
            configureChartAngle();
            configureChartCountBendAndState();
            configureButton();
            rebootCharts();

            add(close, new H3("Реабилитация"),
                    chartAngle, chartCountBendAndState);
        }else{
            add(new H3("Не выбран пациент для" +
                    " отображения графика лечения"));
        }
    }


    //Конфигурация кнопок
    private void configureButton(){
        close.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        close.addClickListener(event -> navigateToPatient());
    }


    //Переход на страницу пациента
    private void navigateToPatient() {
        ComponentUtil.setData(UI.getCurrent(),
                PatientResponseDTO.class, patDTO);
        getUI().get().navigate(PatientView.class);
    }


    //Обновить графики
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


    //Задать значения данными графиков
    private void updateData(){
        Number[] numbersFlexionAngle =
                new Double[dtoCharts.size()];
        Number[] numbersExtensionAngle =
                new Double[dtoCharts.size()];
        Number[] numbersCountBend =
                new Integer[dtoCharts.size()];
        Number[] numbersState =
                new Integer[dtoCharts.size()];

        for (int i = 0; i < dtoCharts.size(); i++){
            numbersFlexionAngle[i] =
                    dtoCharts.get(i).getFlexionAngle();
            numbersExtensionAngle[i] =
                    dtoCharts.get(i).getExtensionAngle();
            numbersCountBend[i] =
                    dtoCharts.get(i).getCountBend();
            numbersState[i] =
                    dtoCharts.get(i).getState();
        }

        dsFlexionAngle.setData(numbersFlexionAngle);
        dsExtensionAngle.setData(numbersExtensionAngle);
        dsCountBend.setData(numbersCountBend);
        dsState.setData(numbersState);
    }


    //Загрузка данных для графиков
    private void loadChart() {
        dtoCharts = doctorService.findAllForCharts(patDTO.getId(),
                scheduleRequest.getElbowKnee(),
                scheduleRequest.getLeftRight());
    }


    //Конфигурация объекта Series
    private void configureDataSeries(){
        dsFlexionAngle.setName("Угол сгибания");
        dsExtensionAngle.setName("Угол разгибания");
        dsCountBend.setName("Количество сгибаний");
        dsState.setName("Боль в суставе");
    }


    //Конфигурация второго графика (количества и боли)
    private void configureChartCountBendAndState() {
        Configuration configuration =
                chartCountBendAndState.getConfiguration();

        configureLegend(configuration,
                "Количество сгибаний/оценка боли в суставе",
                "Количество сгибаний/значение боли");

        configuration.addSeries(dsCountBend);
        configuration.addSeries(dsState);

        chartCountBendAndState.setVisible(false);
    }


    //Конфигурация первого графика (углов)
    public void configureChartAngle(){
        Configuration configuration =
                chartAngle.getConfiguration();

        configureLegend(configuration,
                "Угол сгибания/разгибания", "Угол в градусах");

        configuration.addSeries(dsFlexionAngle);
        configuration.addSeries(dsExtensionAngle);

        chartAngle.setVisible(false);
    }


    //Общая конфигурация графиков
    private void configureLegend(Configuration configuration,
                                 String title, String titleY){
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
