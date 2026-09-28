package com.diabetemanager.ui;

import com.diabetemanager.model.Patient;
import com.diabetemanager.service.ActivityService;
import com.diabetemanager.service.AlertService;
import com.diabetemanager.service.GlucoseService;
import com.diabetemanager.service.MealAnalysisService;
import com.diabetemanager.service.MealService;
import com.diabetemanager.service.StatisticsService;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DiabetesManagerApp extends Application {

    private Patient patient;

    private GlucoseService glucoseService;
    private MealService mealService;
    private ActivityService activityService;
    private StatisticsService statisticsService;
    private AlertService alertService;
    private MealAnalysisService mealAnalysisService;

    private BorderPane root;

    @Override
    public void start(Stage stage) {

        initializeApplication();

        root =
                new BorderPane();

        root.setLeft(
                createSidebar()
        );

        root.setCenter(
                createDashboard()
        );

        Scene scene =
                new Scene(
                        root,
                        1100,
                        700
                );

        stage.setTitle(
                "Diabetes Manager"
        );

        stage.setScene(
                scene
        );

        stage.show();
    }

    private void initializeApplication() {

        patient =
                new Patient(
                        1L,
                        "jan123",
                        "hashedPassword",
                        "Jan",
                        "Kowalski"
                );

        glucoseService =
                new GlucoseService();

        glucoseService.loadMeasurements(
                patient
        );

        mealService =
                new MealService();

        activityService =
                new ActivityService();

        statisticsService =
                new StatisticsService();

        alertService =
                new AlertService();

        mealAnalysisService =
                new MealAnalysisService(
                        glucoseService
                );
    }

    private VBox createSidebar() {

        VBox sidebar =
                new VBox();

        sidebar.setSpacing(
                15
        );

        sidebar.setPadding(
                new Insets(25)
        );

        sidebar.setPrefWidth(
                220
        );

        Label title =
                new Label(
                        "DIABETES\nMANAGER"
                );

        title.setStyle(
                "-fx-font-size: 24px; " +
                        "-fx-font-weight: bold;"
        );

        Button dashboardButton =
                createMenuButton(
                        "Dashboard"
                );

        Button glucoseButton =
                createMenuButton(
                        "Glukoza"
                );

        Button mealButton =
                createMenuButton(
                        "Posiłki"
                );

        Button activityButton =
                createMenuButton(
                        "Aktywność"
                );

        Button statisticsButton =
                createMenuButton(
                        "Statystyki"
                );

        Button alertsButton =
                createMenuButton(
                        "Alerty"
                );

        Button logoutButton =
                createMenuButton(
                        "Wyloguj"
                );

        dashboardButton.setOnAction(
                event ->
                        root.setCenter(
                                createDashboard()
                        )
        );

        glucoseButton.setOnAction(
                event ->
                        root.setCenter(
                                new GlucoseView(
                                        patient,
                                        glucoseService
                                )
                        )
        );

        mealButton.setOnAction(
                event ->
                        showComingSoon(
                                "Moduł posiłków"
                        )
        );

        activityButton.setOnAction(
                event ->
                        showComingSoon(
                                "Moduł aktywności"
                        )
        );

        statisticsButton.setOnAction(
                event ->
                        showComingSoon(
                                "Moduł statystyk"
                        )
        );

        alertsButton.setOnAction(
                event ->
                        showComingSoon(
                                "Moduł alertów"
                        )
        );

        logoutButton.setOnAction(
                event ->
                        showComingSoon(
                                "Wylogowanie"
                        )
        );

        sidebar.getChildren().addAll(
                title,
                dashboardButton,
                glucoseButton,
                mealButton,
                activityButton,
                statisticsButton,
                alertsButton,
                logoutButton
        );

        return sidebar;
    }

    private Button createMenuButton(
            String text) {

        Button button =
                new Button(
                        text
                );

        button.setPrefWidth(
                170
        );

        button.setPrefHeight(
                40
        );

        return button;
    }

    private VBox createDashboard() {

        VBox dashboard =
                new VBox();

        dashboard.setSpacing(
                25
        );

        dashboard.setPadding(
                new Insets(40)
        );

        Label welcome =
                new Label(
                        "Witaj, " +
                                patient.getFirstName() +
                                " " +
                                patient.getLastName()
                );

        welcome.setStyle(
                "-fx-font-size: 30px; " +
                        "-fx-font-weight: bold;"
        );

        Label description =
                new Label(
                        "Panel główny Diabetes Manager"
                );

        description.setStyle(
                "-fx-font-size: 18px;"
        );

        Label glucoseInfo =
                new Label(
                        "Ostatni pomiar glukozy: brak danych"
                );

        Label mealInfo =
                new Label(
                        "Dzisiejsze posiłki: 0"
                );

        Label activityInfo =
                new Label(
                        "Dzisiejsza aktywność: 0 min"
                );

        dashboard.setAlignment(
                Pos.TOP_LEFT
        );

        dashboard.getChildren().addAll(
                welcome,
                description,
                glucoseInfo,
                mealInfo,
                activityInfo
        );

        return dashboard;
    }

    private void showComingSoon(
            String moduleName) {

        javafx.scene.control.Alert alert =
                new javafx.scene.control.Alert(
                        javafx.scene.control.Alert.AlertType.INFORMATION
                );

        alert.setTitle(
                "Diabetes Manager"
        );

        alert.setHeaderText(
                moduleName
        );

        alert.setContentText(
                "Ten moduł zbudujemy w następnym kroku."
        );

        alert.showAndWait();
    }

    public static void main(String[] args) {

        launch();
    }
}