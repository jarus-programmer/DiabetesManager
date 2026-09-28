package com.diabetemanager.ui;

import com.diabetemanager.model.GlucoseMeasurement;
import com.diabetemanager.model.Patient;
import com.diabetemanager.service.GlucoseService;
import com.diabetemanager.util.ValidationUtils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class GlucoseView extends BorderPane {

    private static final double LOW_THRESHOLD =
            70.0;

    private static final double HIGH_THRESHOLD =
            170.0;

    private final Patient patient;
    private final GlucoseService glucoseService;

    private final TableView<GlucoseMeasurement> table;

    private final LineChart<String, Number> chart;

    private final DateTimeFormatter dateFormatter =
            DateTimeFormatter.ofPattern(
                    "dd.MM.yyyy HH:mm"
            );

    public GlucoseView(
            Patient patient,
            GlucoseService glucoseService) {

        this.patient = patient;
        this.glucoseService = glucoseService;

        this.table =
                new TableView<>();

        CategoryAxis xAxis =
                new CategoryAxis();

        NumberAxis yAxis =
                new NumberAxis();

        yAxis.setLabel(
                "Glukoza [mg/dL]"
        );

        this.chart =
                new LineChart<>(
                        xAxis,
                        yAxis
                );

        createLayout();
        refreshData();
    }

    private void createLayout() {

        setPadding(
                new Insets(30)
        );

        Label title =
                new Label(
                        "Pomiary glukozy"
                );

        title.setStyle(
                "-fx-font-size: 28px; " +
                        "-fx-font-weight: bold;"
        );

        Label description =
                new Label(
                        "Historia pomiarów i wykres poziomu glukozy"
                );

        description.setStyle(
                "-fx-font-size: 15px;"
        );

        VBox header =
                new VBox(
                        5,
                        title,
                        description
                );

        setTop(header);

        createTable();

        chart.setTitle(
                "Poziom glukozy w czasie"
        );

        chart.setLegendVisible(
                true
        );

        chart.setAnimated(
                false
        );

        chart.setPrefHeight(
                350
        );

        VBox content =
                new VBox(
                        20,
                        table,
                        chart
                );

        VBox.setVgrow(
                table,
                javafx.scene.layout.Priority.ALWAYS
        );

        VBox.setVgrow(
                chart,
                javafx.scene.layout.Priority.ALWAYS
        );

        setCenter(content);

        HBox addPanel =
                createAddPanel();

        setBottom(addPanel);
    }

    private void createTable() {

        TableColumn<
                GlucoseMeasurement,
                String
                > idColumn =
                new TableColumn<>("ID");

        idColumn.setCellValueFactory(
                cellData ->
                        new javafx.beans.property
                                .SimpleStringProperty(
                                String.valueOf(
                                        cellData
                                                .getValue()
                                                .getId()
                                )
                        )
        );

        TableColumn<
                GlucoseMeasurement,
                String
                > glucoseColumn =
                new TableColumn<>(
                        "Glukoza"
                );

        glucoseColumn.setCellValueFactory(
                cellData ->
                        new javafx.beans.property
                                .SimpleStringProperty(
                                String.format(
                                        "%.2f mg/dL",
                                        cellData
                                                .getValue()
                                                .getGlucoseLevel()
                                )
                        )
        );

        TableColumn<
                GlucoseMeasurement,
                String
                > statusColumn =
                new TableColumn<>(
                        "Status"
                );

        statusColumn.setCellValueFactory(
                cellData ->
                        new javafx.beans.property
                                .SimpleStringProperty(
                                getGlucoseStatus(
                                        cellData
                                                .getValue()
                                                .getGlucoseLevel()
                                )
                        )
        );

        TableColumn<
                GlucoseMeasurement,
                String
                > dateColumn =
                new TableColumn<>(
                        "Data"
                );

        dateColumn.setCellValueFactory(
                cellData ->
                        new javafx.beans.property
                                .SimpleStringProperty(
                                cellData
                                        .getValue()
                                        .getDateTime()
                                        .format(
                                                dateFormatter
                                        )
                        )
        );

        idColumn.setPrefWidth(
                80
        );

        glucoseColumn.setPrefWidth(
                160
        );

        statusColumn.setPrefWidth(
                180
        );

        dateColumn.setPrefWidth(
                220
        );

        table.getColumns().addAll(
                idColumn,
                glucoseColumn,
                statusColumn,
                dateColumn
        );

        table.setColumnResizePolicy(
                TableView
                        .CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN
        );

        table.setPlaceholder(
                new Label(
                        "Brak zapisanych pomiarów."
                )
        );

        table.setPrefHeight(
                300
        );
    }

    private HBox createAddPanel() {

        TextField glucoseField =
                new TextField();

        glucoseField.setPromptText(
                "np. 125"
        );

        glucoseField.setPrefWidth(
                180
        );

        Button addButton =
                new Button(
                        "Dodaj pomiar"
                );

        addButton.setPrefHeight(
                35
        );

        addButton.setOnAction(
                event ->
                        addMeasurement(
                                glucoseField
                        )
        );

        HBox panel =
                new HBox(
                        15,
                        new Label(
                                "Glukoza [mg/dL]:"
                        ),
                        glucoseField,
                        addButton
                );

        panel.setAlignment(
                Pos.CENTER_LEFT
        );

        panel.setPadding(
                new Insets(
                        20,
                        0,
                        0,
                        0
                )
        );

        return panel;
    }

    private void addMeasurement(
            TextField glucoseField) {

        String text =
                glucoseField
                        .getText()
                        .trim()
                        .replace(',', '.');

        if (text.isEmpty()) {

            showError(
                    "Pole glukozy nie może być puste."
            );

            return;
        }

        double glucose;

        try {

            glucose =
                    Double.parseDouble(
                            text
                    );

        } catch (NumberFormatException e) {

            showError(
                    "Wpisz poprawną wartość liczbową."
            );

            return;
        }

        try {

            ValidationUtils.validateGlucose(
                    glucose
            );

        } catch (RuntimeException e) {

            showError(
                    e.getMessage()
            );

            return;
        }

        GlucoseMeasurement measurement =
                new GlucoseMeasurement(
                        generateMeasurementId(),
                        glucose,
                        LocalDateTime.now()
                );

        try {

            glucoseService.addMeasurement(
                    patient,
                    measurement
            );

            refreshData();

            glucoseField.clear();

        } catch (RuntimeException e) {

            showError(
                    e.getMessage()
            );
        }
    }

    private void refreshData() {

        refreshTable();

        refreshChart();
    }

    private void refreshTable() {

        List<GlucoseMeasurement> measurements =
                glucoseService.getMeasurements(
                        patient
                );

        ObservableList<
                GlucoseMeasurement
                > data =
                FXCollections.observableArrayList(
                        measurements
                );

        table.setItems(
                data
        );
    }

    private void refreshChart() {

        chart.getData().clear();

        List<GlucoseMeasurement> measurements =
                glucoseService.getMeasurements(
                        patient
                );

        if (measurements.isEmpty()) {
            return;
        }

        XYChart.Series<String, Number> glucoseSeries =
                new XYChart.Series<>();

        glucoseSeries.setName(
                "Glukoza"
        );

        XYChart.Series<String, Number> lowSeries =
                new XYChart.Series<>();

        lowSeries.setName(
                "Granica niska: 70"
        );

        XYChart.Series<String, Number> highSeries =
                new XYChart.Series<>();

        highSeries.setName(
                "Granica wysoka: 170"
        );

        for (GlucoseMeasurement measurement :
                measurements) {

            String date =
                    measurement
                            .getDateTime()
                            .format(
                                    dateFormatter
                            );

            glucoseSeries
                    .getData()
                    .add(
                            new XYChart.Data<>(
                                    date,
                                    measurement
                                            .getGlucoseLevel()
                            )
                    );

            lowSeries
                    .getData()
                    .add(
                            new XYChart.Data<>(
                                    date,
                                    LOW_THRESHOLD
                            )
                    );

            highSeries
                    .getData()
                    .add(
                            new XYChart.Data<>(
                                    date,
                                    HIGH_THRESHOLD
                            )
                    );
        }

        chart.getData().addAll(
                glucoseSeries,
                lowSeries,
                highSeries
        );
    }

    private String getGlucoseStatus(
            double glucose) {

        if (glucose < LOW_THRESHOLD) {

            return "NISKI";
        }

        if (glucose > HIGH_THRESHOLD) {

            return "POWYŻEJ 170";
        }

        return "OK";
    }

    private long generateMeasurementId() {

        return patient
                .getGlucoseMeasurements()
                .stream()
                .mapToLong(
                        measurement ->
                                measurement.getId()
                )
                .max()
                .orElse(0L) + 1L;
    }

    private void showError(
            String message) {

        Alert alert =
                new Alert(
                        Alert.AlertType.ERROR
                );

        alert.setTitle(
                "Błąd"
        );

        alert.setHeaderText(
                "Nie udało się wykonać operacji"
        );

        alert.setContentText(
                message
        );

        alert.showAndWait();
    }
}