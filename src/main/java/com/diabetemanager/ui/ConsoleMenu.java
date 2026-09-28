package com.diabetemanager.ui;

import com.diabetemanager.model.FoodProduct;
import com.diabetemanager.model.GlucoseMeasurement;
import com.diabetemanager.model.Meal;
import com.diabetemanager.model.MealGlucoseAnalysis;
import com.diabetemanager.model.MealItem;
import com.diabetemanager.model.Patient;
import com.diabetemanager.model.PhysicalActivity;

import com.diabetemanager.service.ActivityService;
import com.diabetemanager.service.AlertService;
import com.diabetemanager.service.GlucoseService;
import com.diabetemanager.service.MealAnalysisService;
import com.diabetemanager.service.MealService;
import com.diabetemanager.service.StatisticsService;

import com.diabetemanager.util.ValidationUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class ConsoleMenu {

    private final Patient patient;

    private final GlucoseService glucoseService;
    private final MealService mealService;
    private final ActivityService activityService;
    private final StatisticsService statisticsService;
    private final AlertService alertService;
    private final MealAnalysisService mealAnalysisService;

    private final Scanner scanner;

    private final DateTimeFormatter dateFormatter =
            DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    public ConsoleMenu(
            Patient patient,
            GlucoseService glucoseService,
            MealService mealService,
            ActivityService activityService,
            StatisticsService statisticsService,
            AlertService alertService,
            MealAnalysisService mealAnalysisService) {

        this.patient = patient;
        this.glucoseService = glucoseService;
        this.mealService = mealService;
        this.activityService = activityService;
        this.statisticsService = statisticsService;
        this.alertService = alertService;
        this.mealAnalysisService = mealAnalysisService;

        this.scanner = new Scanner(System.in);
    }

    public void start() {

        boolean running = true;

        while (running) {

            displayMenu();

            int choice =
                    readInt("Wybierz opcję: ");

            System.out.println();

            switch (choice) {

                case 1:
                    addGlucoseMeasurement();
                    break;

                case 2:
                    showGlucoseMeasurements();
                    break;

                case 3:
                    addMeal();
                    break;

                case 4:
                    showMeals();
                    break;

                case 5:
                    addPhysicalActivity();
                    break;

                case 6:
                    showPhysicalActivities();
                    break;

                case 7:
                    showStatistics();
                    break;

                case 8:
                    showAlerts();
                    break;

                case 9:
                    showPatientInformation();
                    break;

                case 10:
                    removeGlucoseMeasurement();
                    break;

                case 11:
                    removeMeal();
                    break;

                case 12:
                    removePhysicalActivity();
                    break;

                case 13:
                    showMealAnalysis();
                    break;

                case 14:
                    showAllMealsAnalysis();
                    break;

                case 0:
                    running = false;

                    System.out.println(
                            "Zamykanie Diabetes Manager..."
                    );

                    break;

                default:
                    System.out.println(
                            "Nieprawidłowa opcja."
                    );
            }

            System.out.println();
        }

        scanner.close();
    }

    private void displayMenu() {

        System.out.println("=================================");
        System.out.println("       DIABETES MANAGER");
        System.out.println("=================================");
        System.out.println("1. Dodaj pomiar glukozy");
        System.out.println("2. Pokaż pomiary glukozy");
        System.out.println("3. Dodaj posiłek");
        System.out.println("4. Pokaż posiłki");
        System.out.println("5. Dodaj aktywność");
        System.out.println("6. Pokaż aktywności");
        System.out.println("7. Pokaż statystyki");
        System.out.println("8. Pokaż alerty");
        System.out.println("9. Informacje o pacjencie");
        System.out.println("10. Usuń pomiar glukozy");
        System.out.println("11. Usuń posiłek");
        System.out.println("12. Usuń aktywność");
        System.out.println("13. Analiza posiłku");
        System.out.println("14. Analiza wszystkich posiłków");
        System.out.println("0. Wyjście");
        System.out.println("=================================");
    }

    private void addGlucoseMeasurement() {

        System.out.println(
                "---- DODAJ POMIAR GLUKOZY ----"
        );

        double glucose =
                readDouble(
                        "Poziom glukozy [mg/dL]: "
                );

        try {

            ValidationUtils.validateGlucose(
                    glucose
            );

        } catch (RuntimeException e) {

            System.out.println(
                    "Nieprawidłowy poziom glukozy: "
                            + glucose
            );

            System.out.println(
                    "Powrót do menu głównego."
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

            System.out.println(
                    "Pomiar został dodany."
            );

        } catch (RuntimeException e) {

            System.out.println(
                    "Nie udało się dodać pomiaru: "
                            + e.getMessage()
            );
        }
    }

    private void showGlucoseMeasurements() {

        System.out.println(
                "---- POMIARY GLUKOZY ----"
        );

        List<GlucoseMeasurement> measurements =
                glucoseService.getMeasurements(patient);

        if (measurements.isEmpty()) {

            System.out.println(
                    "Brak zapisanych pomiarów."
            );

            return;
        }

        for (GlucoseMeasurement measurement :
                measurements) {

            System.out.println(
                    "ID: " +
                            measurement.getId()
            );

            System.out.println(
                    "Glukoza: " +
                            measurement.getGlucoseLevel() +
                            " mg/dL"
            );

            System.out.println(
                    "Data: " +
                            measurement
                                    .getDateTime()
                                    .format(dateFormatter)
            );

            System.out.println(
                    "---------------------------------"
            );
        }
    }

    private void removeGlucoseMeasurement() {

        System.out.println(
                "---- USUŃ POMIAR GLUKOZY ----"
        );

        List<GlucoseMeasurement> measurements =
                glucoseService.getMeasurements(patient);

        if (measurements.isEmpty()) {

            System.out.println(
                    "Brak pomiarów do usunięcia."
            );

            return;
        }

        showGlucoseMeasurements();

        long id =
                readLong(
                        "Podaj ID pomiaru do usunięcia: "
                );

        GlucoseMeasurement measurement =
                glucoseService.findMeasurementById(
                        patient,
                        id
                );

        if (measurement == null) {

            System.out.println(
                    "Nie znaleziono pomiaru o ID: " + id
            );

            return;
        }

        glucoseService.removeMeasurement(
                patient,
                id
        );

        System.out.println(
                "Pomiar został usunięty."
        );
    }

    private GlucoseMeasurement chooseGlucoseMeasurement() {

        System.out.println();

        System.out.println(
                "Wybierz sposób powiązania pomiaru:"
        );

        System.out.println(
                "1. Wybierz istniejący pomiar"
        );

        System.out.println(
                "2. Wykonaj nowy pomiar"
        );

        System.out.println(
                "0. Anuluj"
        );

        int choice =
                readInt("Wybierz opcję: ");

        switch (choice) {

            case 1:
                return chooseExistingGlucoseMeasurement();

            case 2:
                return createGlucoseMeasurement();

            case 0:
                return null;

            default:

                System.out.println(
                        "Nieprawidłowa opcja."
                );

                return null;
        }
    }

    private GlucoseMeasurement chooseExistingGlucoseMeasurement() {

        Duration recentWindow =
                Duration.ofHours(2);

        List<GlucoseMeasurement> recentMeasurements =
                glucoseService.getRecentMeasurements(
                        patient,
                        recentWindow
                );

        System.out.println();

        if (recentMeasurements.isEmpty()) {

            System.out.println(
                    "Brak pomiarów z ostatnich 2 godzin."
            );

            System.out.println();

            System.out.println(
                    "1. Wykonaj nowy pomiar"
            );

            System.out.println(
                    "2. Pokaż starsze pomiary"
            );

            System.out.println(
                    "0. Anuluj"
            );

            int choice =
                    readInt("Wybierz opcję: ");

            switch (choice) {

                case 1:
                    return createGlucoseMeasurement();

                case 2:
                    return chooseOlderGlucoseMeasurement();

                case 0:
                    return null;

                default:

                    System.out.println(
                            "Nieprawidłowa opcja."
                    );

                    return null;
            }
        }

        System.out.println(
                "---- ŚWIEŻE POMIARY ----"
        );

        for (GlucoseMeasurement measurement :
                recentMeasurements) {

            System.out.println(
                    "ID: " +
                            measurement.getId() +
                            " | Glukoza: " +
                            measurement.getGlucoseLevel() +
                            " mg/dL | Data: " +
                            measurement
                                    .getDateTime()
                                    .format(dateFormatter)
            );
        }

        System.out.println();

        System.out.println(
                "1. Wybierz świeży pomiar"
        );

        System.out.println(
                "2. Pokaż starsze pomiary"
        );

        System.out.println(
                "0. Anuluj"
        );

        int choice =
                readInt("Wybierz opcję: ");

        switch (choice) {

            case 1:

                long id =
                        readLong(
                                "Podaj ID pomiaru: "
                        );

                GlucoseMeasurement measurement =
                        glucoseService.findMeasurementById(
                                patient,
                                id
                        );

                if (measurement == null) {

                    System.out.println(
                            "Nie znaleziono pomiaru."
                    );

                    return null;
                }

                if (!recentMeasurements.contains(
                        measurement)) {

                    System.out.println(
                            "Wybrany pomiar nie znajduje się "
                                    + "w grupie świeżych pomiarów."
                    );

                    return null;
                }

                return measurement;

            case 2:
                return chooseOlderGlucoseMeasurement();

            case 0:
                return null;

            default:

                System.out.println(
                        "Nieprawidłowa opcja."
                );

                return null;
        }
    }

    private GlucoseMeasurement chooseOlderGlucoseMeasurement() {

        System.out.println();

        System.out.println(
                "---- STARSZE POMIARY ----"
        );

        List<GlucoseMeasurement> allMeasurements =
                glucoseService.getMeasurements(patient);

        if (allMeasurements.isEmpty()) {

            System.out.println(
                    "Brak zapisanych pomiarów."
            );

            return null;
        }

        Duration recentWindow =
                Duration.ofHours(2);

        List<GlucoseMeasurement> recentMeasurements =
                glucoseService.getRecentMeasurements(
                        patient,
                        recentWindow
                );

        List<GlucoseMeasurement> olderMeasurements =
                allMeasurements
                        .stream()
                        .filter(measurement ->
                                !recentMeasurements.contains(
                                        measurement
                                )
                        )
                        .sorted(
                                (first, second) ->
                                        second
                                                .getDateTime()
                                                .compareTo(
                                                        first.getDateTime()
                                                )
                        )
                        .toList();

        if (olderMeasurements.isEmpty()) {

            System.out.println(
                    "Brak starszych pomiarów."
            );

            return null;
        }

        for (GlucoseMeasurement measurement :
                olderMeasurements) {

            System.out.println(
                    "ID: " +
                            measurement.getId() +
                            " | Glukoza: " +
                            measurement.getGlucoseLevel() +
                            " mg/dL | Data: " +
                            measurement
                                    .getDateTime()
                                    .format(dateFormatter)
            );
        }

        System.out.println();

        long id =
                readLong(
                        "Podaj ID pomiaru: "
                );

        GlucoseMeasurement measurement =
                glucoseService.findMeasurementById(
                        patient,
                        id
                );

        if (measurement == null) {

            System.out.println(
                    "Nie znaleziono pomiaru."
            );

            return null;
        }

        System.out.println();

        System.out.println(
                "Uwaga: wybrany pomiar jest starszy "
                        + "niż 2 godziny."
        );

        System.out.println(
                "Czy chcesz użyć tego pomiaru?"
        );

        System.out.println(
                "1. Tak"
        );

        System.out.println(
                "0. Nie"
        );

        int confirmation =
                readInt("Wybierz opcję: ");

        if (confirmation == 1) {
            return measurement;
        }

        return null;
    }

    private GlucoseMeasurement createGlucoseMeasurement() {

        System.out.println();
        System.out.println(
                "---- NOWY POMIAR GLUKOZY ----"
        );

        double glucose =
                readDouble(
                        "Poziom glukozy [mg/dL]: "
                );

        try {

            ValidationUtils.validateGlucose(
                    glucose
            );

        } catch (RuntimeException e) {

            System.out.println(
                    "Nieprawidłowy poziom glukozy."
            );

            System.out.println(
                    "Powrót do menu głównego."
            );

            return null;
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

            System.out.println(
                    "Pomiar został zapisany."
            );

            return measurement;

        } catch (RuntimeException e) {

            System.out.println(
                    "Nie udało się zapisać pomiaru: "
                            + e.getMessage()
            );

            return null;
        }
    }

    private void addMeal() {

        System.out.println(
                "---- DODAJ POSIŁEK ----"
        );

        GlucoseMeasurement glucoseMeasurement =
                chooseGlucoseMeasurement();

        if (glucoseMeasurement == null) {

            System.out.println(
                    "Nie wybrano pomiaru glukozy."
            );

            System.out.println(
                    "Powrót do menu głównego."
            );

            return;
        }

        System.out.print(
                "Nazwa posiłku: "
        );

        String name =
                scanner.nextLine();

        if (name.isBlank()) {

            System.out.println(
                    "Nazwa posiłku nie może być pusta."
            );

            return;
        }

        Meal meal =
                new Meal(
                        generateMealId(),
                        name,
                        LocalDateTime.now(),
                        glucoseMeasurement.getId()
                );

        boolean addingProducts = true;
        boolean hasProducts = false;

        while (addingProducts) {

            FoodProduct selectedProduct =
                    chooseFoodProduct();

            if (selectedProduct == null) {

                if (!hasProducts) {

                    System.out.println(
                            "Posiłek musi zawierać przynajmniej jeden produkt."
                    );

                    return;
                }

                break;
            }

            double amount =
                    readDouble(
                            "Podaj ilość [g]: "
                    );

            try {

                ValidationUtils.validatePositive(
                        amount,
                        "Ilość produktu"
                );

            } catch (RuntimeException e) {

                System.out.println(
                        e.getMessage()
                );

                continue;
            }

            MealItem mealItem =
                    new MealItem(
                            selectedProduct,
                            amount
                    );

            try {

                mealService.addMealItem(
                        meal,
                        mealItem
                );

                hasProducts = true;

                System.out.println();
                System.out.println(
                        "Produkt został dodany."
                );

                System.out.println(
                        "Węglowodany: " +
                                String.format(
                                        "%.2f",
                                        mealItem.calculateCarbohydrates()
                                ) +
                                " g"
                );

                System.out.println(
                        "Kalorie: " +
                                String.format(
                                        "%.2f",
                                        mealItem.calculateCalories()
                                ) +
                                " kcal"
                );

            } catch (RuntimeException e) {

                System.out.println(
                        "Nie udało się dodać produktu: "
                                + e.getMessage()
                );

                continue;
            }

            System.out.println();

            System.out.println(
                    "Dodać kolejny produkt?"
            );

            System.out.println(
                    "1. Tak"
            );

            System.out.println(
                    "0. Nie"
            );

            int choice =
                    readInt("Wybierz opcję: ");

            if (choice != 1) {
                addingProducts = false;
            }
        }

        try {

            mealService.addMeal(
                    patient,
                    meal
            );

            System.out.println();
            System.out.println(
                    "Posiłek został dodany."
            );

            System.out.println(
                    "Powiązany pomiar glukozy: " +
                            glucoseMeasurement.getGlucoseLevel() +
                            " mg/dL"
            );

            System.out.println(
                    "Łączne węglowodany: " +
                            String.format(
                                    "%.2f",
                                    meal.getTotalCarbohydrates()
                            ) +
                            " g"
            );

            System.out.println(
                    "Łączne kalorie: " +
                            String.format(
                                    "%.2f",
                                    meal.getTotalCalories()
                            ) +
                            " kcal"
            );

        } catch (RuntimeException e) {

            System.out.println(
                    "Nie udało się dodać posiłku: " +
                            e.getMessage()
            );
        }
    }

    private FoodProduct chooseFoodProduct() {

        List<FoodProduct> products =
                getAvailableFoodProducts();

        System.out.println();
        System.out.println(
                "---- WYBIERZ PRODUKT ----"
        );

        for (int i = 0; i < products.size(); i++) {

            FoodProduct product =
                    products.get(i);

            System.out.println(
                    (i + 1) +
                            ". " +
                            product.getName() +
                            " | Węglowodany: " +
                            product.getCarbohydratesPer100g() +
                            " g/100g | Kalorie: " +
                            product.getCaloriesPer100g() +
                            " kcal/100g"
            );
        }

        System.out.println(
                "0. Anuluj"
        );

        int choice =
                readInt("Wybierz produkt: ");

        if (choice == 0) {
            return null;
        }

        if (choice < 1 || choice > products.size()) {

            System.out.println(
                    "Nieprawidłowy wybór produktu."
            );

            return null;
        }

        return products.get(choice - 1);
    }

    private List<FoodProduct> getAvailableFoodProducts() {

        return List.of(

                new FoodProduct(
                        1L,
                        "Ryż",
                        28.0,
                        130.0
                ),

                new FoodProduct(
                        2L,
                        "Kurczak",
                        0.0,
                        165.0
                ),

                new FoodProduct(
                        3L,
                        "Pomidor",
                        3.9,
                        18.0
                ),

                new FoodProduct(
                        4L,
                        "Banan",
                        22.8,
                        89.0
                ),

                new FoodProduct(
                        5L,
                        "Jabłko",
                        13.8,
                        52.0
                ),

                new FoodProduct(
                        6L,
                        "Płatki owsiane",
                        66.0,
                        389.0
                ),

                new FoodProduct(
                        7L,
                        "Mleko",
                        4.8,
                        61.0
                ),

                new FoodProduct(
                        8L,
                        "Chleb",
                        49.0,
                        247.0
                ),

                new FoodProduct(
                        9L,
                        "Jajko",
                        1.1,
                        155.0
                ),

                new FoodProduct(
                        10L,
                        "Ziemniaki",
                        17.0,
                        77.0
                )
        );
    }

    private void showMeals() {

        System.out.println(
                "---- POSIŁKI ----"
        );

        List<Meal> meals =
                mealService.getMeals(patient);

        if (meals.isEmpty()) {

            System.out.println(
                    "Brak zapisanych posiłków."
            );

            return;
        }

        for (Meal meal : meals) {

            System.out.println();

            System.out.println(
                    "ID: " +
                            meal.getId()
            );

            System.out.println(
                    "Nazwa: " +
                            meal.getName()
            );

            GlucoseMeasurement relatedMeasurement =
                    glucoseService.findMeasurementById(
                            patient,
                            meal.getGlucoseMeasurementId()
                    );

            if (relatedMeasurement != null) {

                System.out.println(
                        "Glukoza przed posiłkiem: " +
                                relatedMeasurement.getGlucoseLevel() +
                                " mg/dL"
                );
            }

            System.out.println(
                    "Data: " +
                            meal.getDateTime()
                                    .format(dateFormatter)
            );

            System.out.println(
                    "Produkty:"
            );

            if (meal.getItems().isEmpty()) {

                System.out.println(
                        "  Brak produktów."
                );

            } else {

                for (MealItem item :
                        meal.getItems()) {

                    System.out.println(
                            "  - " +
                                    item.getFoodProduct().getName() +
                                    " | " +
                                    item.getAmountInGrams() +
                                    " g | Węglowodany: " +
                                    String.format(
                                            "%.2f",
                                            item.calculateCarbohydrates()
                                    ) +
                                    " g | Kalorie: " +
                                    String.format(
                                            "%.2f",
                                            item.calculateCalories()
                                    ) +
                                    " kcal"
                    );
                }
            }

            System.out.println(
                    "Łączne węglowodany: " +
                            String.format(
                                    "%.2f",
                                    meal.getTotalCarbohydrates()
                            ) +
                            " g"
            );

            System.out.println(
                    "Łączne kalorie: " +
                            String.format(
                                    "%.2f",
                                    meal.getTotalCalories()
                            ) +
                            " kcal"
            );

            System.out.println(
                    "---------------------------------"
            );
        }
    }

    private void removeMeal() {

        System.out.println(
                "---- USUŃ POSIŁEK ----"
        );

        List<Meal> meals =
                mealService.getMeals(patient);

        if (meals.isEmpty()) {

            System.out.println(
                    "Brak posiłków do usunięcia."
            );

            return;
        }

        showMeals();

        long id =
                readLong(
                        "Podaj ID posiłku do usunięcia: "
                );

        Meal meal =
                mealService.findMealById(
                        patient,
                        id
                );

        if (meal == null) {

            System.out.println(
                    "Nie znaleziono posiłku o ID: " +
                            id
            );

            return;
        }

        mealService.removeMeal(
                patient,
                id
        );

        System.out.println(
                "Posiłek został usunięty."
        );
    }

    private void addPhysicalActivity() {

        System.out.println(
                "---- DODAJ AKTYWNOŚĆ ----"
        );

        System.out.print(
                "Rodzaj aktywności: "
        );

        String type =
                scanner.nextLine();

        int duration =
                readInt(
                        "Czas trwania [min]: "
                );

        System.out.print(
                "Intensywność: "
        );

        String intensity =
                scanner.nextLine();

        double calories =
                readDouble(
                        "Spalone kalorie: "
                );

        PhysicalActivity activity =
                new PhysicalActivity(
                        generateActivityId(),
                        type,
                        duration,
                        intensity,
                        calories,
                        LocalDateTime.now()
                );

        try {

            activityService.addActivity(
                    patient,
                    activity
            );

            System.out.println(
                    "Aktywność została dodana."
            );

        } catch (RuntimeException e) {

            System.out.println(
                    "Nie udało się dodać aktywności: "
                            + e.getMessage()
            );
        }
    }

    private void showPhysicalActivities() {

        System.out.println(
                "---- AKTYWNOŚCI FIZYCZNE ----"
        );

        List<PhysicalActivity> activities =
                activityService.getActivities(patient);

        if (activities.isEmpty()) {

            System.out.println(
                    "Brak zapisanych aktywności."
            );

            return;
        }

        for (PhysicalActivity activity :
                activities) {

            System.out.println(
                    "ID: " +
                            activity.getId()
            );

            System.out.println(
                    "Rodzaj: " +
                            activity.getType()
            );

            System.out.println(
                    "Czas: " +
                            activity.getDurationMinutes() +
                            " min"
            );

            System.out.println(
                    "Intensywność: " +
                            activity.getIntensity()
            );

            System.out.println(
                    "Spalone kalorie: " +
                            activity.getCaloriesBurned() +
                            " kcal"
            );

            System.out.println(
                    "Data: " +
                            activity.getDateTime()
                                    .format(dateFormatter)
            );

            System.out.println(
                    "---------------------------------"
            );
        }
    }

    private void removePhysicalActivity() {

        System.out.println(
                "---- USUŃ AKTYWNOŚĆ ----"
        );

        List<PhysicalActivity> activities =
                activityService.getActivities(patient);

        if (activities.isEmpty()) {

            System.out.println(
                    "Brak aktywności do usunięcia."
            );

            return;
        }

        showPhysicalActivities();

        long id =
                readLong(
                        "Podaj ID aktywności do usunięcia: "
                );

        PhysicalActivity activity =
                activityService.findActivityById(
                        patient,
                        id
                );

        if (activity == null) {

            System.out.println(
                    "Nie znaleziono aktywności o ID: " +
                            id
            );

            return;
        }

        activityService.removeActivity(
                patient,
                id
        );

        System.out.println(
                "Aktywność została usunięta."
        );
    }

    private void showStatistics() {

        System.out.println(
                "---- STATYSTYKI ----"
        );

        System.out.println();

        System.out.println(
                "Pomiary glukozy: " +
                        glucoseService
                                .getMeasurements(patient)
                                .size()
        );

        System.out.println(
                "Średnia glukoza: " +
                        statisticsService
                                .calculateAverageGlucose(patient)
        );

        System.out.println(
                "Minimum: " +
                        statisticsService
                                .getMinimumGlucose(patient)
        );

        System.out.println(
                "Maximum: " +
                        statisticsService
                                .getMaximumGlucose(patient)
        );

        System.out.println();

        System.out.println(
                "Posiłki: " +
                        mealService
                                .getMeals(patient)
                                .size()
        );

        System.out.println(
                "Węglowodany: " +
                        String.format(
                                "%.2f",
                                mealService
                                        .calculateTotalCarbohydrates(patient)
                        ) +
                        " g"
        );

        System.out.println(
                "Kalorie: " +
                        String.format(
                                "%.2f",
                                mealService
                                        .calculateTotalCalories(patient)
                        ) +
                        " kcal"
        );

        System.out.println();

        System.out.println(
                "Aktywności: " +
                        activityService
                                .getActivities(patient)
                                .size()
        );

        System.out.println(
                "Czas aktywności: " +
                        activityService
                                .calculateTotalDuration(patient) +
                        " min"
        );

        System.out.println(
                "Spalone kalorie: " +
                        activityService
                                .calculateTotalCaloriesBurned(patient) +
                        " kcal"
        );
    }

    private void showAlerts() {

        System.out.println(
                "---- ALERTY INFORMACYJNE ----"
        );

        double minGlucose = 70.0;
        double maxGlucose = 180.0;

        int repeatedMeasurements = 2;

        Duration maximumGap =
                Duration.ofHours(24);

        List<String> alerts =
                alertService.generateAlerts(
                        patient,
                        minGlucose,
                        maxGlucose,
                        repeatedMeasurements,
                        maximumGap
                );

        if (alerts.isEmpty()) {

            System.out.println(
                    "Brak alertów informacyjnych."
            );

            return;
        }

        for (String alert : alerts) {

            System.out.println(
                    "- " + alert
            );
        }
    }

    private void showPatientInformation() {

        System.out.println(
                "---- INFORMACJE O PACJENCIE ----"
        );

        System.out.println(
                "ID: " +
                        patient.getId()
        );

        System.out.println(
                "Imię: " +
                        patient.getFirstName()
        );

        System.out.println(
                "Nazwisko: " +
                        patient.getLastName()
        );

        System.out.println(
                "Login: " +
                        patient.getUsername()
        );

        System.out.println(
                "Rola: " +
                        patient.getRole()
        );
    }

    private void showMealAnalysis() {

        System.out.println(
                "---- ANALIZA POSIŁKU ----"
        );

        List<Meal> meals =
                mealService.getMeals(patient);

        if (meals.isEmpty()) {

            System.out.println(
                    "Brak zapisanych posiłków."
            );

            return;
        }

        showMeals();

        long mealId =
                readLong(
                        "Podaj ID posiłku do analizy: "
                );

        MealGlucoseAnalysis analysis =
                mealAnalysisService.analyzeMeal(
                        patient,
                        mealId,
                        Duration.ofHours(2)
                );

        if (analysis == null) {

            System.out.println(
                    "Nie udało się znaleźć danych do analizy."
            );

            return;
        }

        System.out.println();

        System.out.println(
                "Posiłek: " +
                        analysis.getMeal().getName()
        );

        System.out.println(
                "Data posiłku: " +
                        analysis.getMeal()
                                .getDateTime()
                                .format(dateFormatter)
        );

        System.out.println(
                "Glukoza przed posiłkiem: " +
                        analysis
                                .getBeforeMealMeasurement()
                                .getGlucoseLevel() +
                        " mg/dL"
        );

        if (!analysis.hasAfterMealMeasurement()) {

            System.out.println(
                    "Brak pomiaru glukozy po posiłku "
                            + "w ciągu 2 godzin."
            );

            return;
        }

        System.out.println(
                "Glukoza po posiłku: " +
                        analysis
                                .getAfterMealMeasurement()
                                .getGlucoseLevel() +
                        " mg/dL"
        );

        System.out.println(
                "Data pomiaru po posiłku: " +
                        analysis
                                .getAfterMealMeasurement()
                                .getDateTime()
                                .format(dateFormatter)
        );

        System.out.println(
                "Różnica: " +
                        analysis.getGlucoseDifference() +
                        " mg/dL"
        );
    }

    private void showAllMealsAnalysis() {

        System.out.println(
                "---- ANALIZA WSZYSTKICH POSIŁKÓW ----"
        );

        List<MealGlucoseAnalysis> analyses =
                mealAnalysisService.analyzeAllMeals(
                        patient,
                        Duration.ofHours(2)
                );

        if (analyses.isEmpty()) {

            System.out.println(
                    "Brak danych do analizy."
            );

            return;
        }

        for (MealGlucoseAnalysis analysis :
                analyses) {

            System.out.println();

            System.out.println(
                    "Posiłek: " +
                            analysis.getMeal().getName()
            );

            System.out.println(
                    "Data: " +
                            analysis.getMeal()
                                    .getDateTime()
                                    .format(dateFormatter)
            );

            System.out.println(
                    "Glukoza przed: " +
                            analysis
                                    .getBeforeMealMeasurement()
                                    .getGlucoseLevel() +
                            " mg/dL"
            );

            if (analysis.hasAfterMealMeasurement()) {

                System.out.println(
                        "Glukoza po: " +
                                analysis
                                        .getAfterMealMeasurement()
                                        .getGlucoseLevel() +
                                " mg/dL"
                );

                System.out.println(
                        "Różnica: " +
                                analysis.getGlucoseDifference() +
                                " mg/dL"
                );

            } else {

                System.out.println(
                        "Glukoza po: brak pomiaru "
                                + "w ciągu 2 godzin."
                );
            }

            System.out.println(
                    "---------------------------------"
            );
        }
    }

    private int readInt(String message) {

        while (true) {

            try {

                System.out.print(message);

                String input =
                        scanner.nextLine();

                return Integer.parseInt(input);

            } catch (NumberFormatException e) {

                System.out.println(
                        "Wpisz poprawną liczbę całkowitą."
                );
            }
        }
    }

    private long readLong(String message) {

        while (true) {

            try {

                System.out.print(message);

                String input =
                        scanner.nextLine();

                return Long.parseLong(input);

            } catch (NumberFormatException e) {

                System.out.println(
                        "Wpisz poprawne ID."
                );
            }
        }
    }

    private double readDouble(String message) {

        while (true) {

            try {

                System.out.print(message);

                String input =
                        scanner.nextLine()
                                .replace(',', '.');

                return Double.parseDouble(input);

            } catch (NumberFormatException e) {

                System.out.println(
                        "Wpisz poprawną liczbę."
                );
            }
        }
    }

    private long generateMeasurementId() {

        return patient
                .getGlucoseMeasurements()
                .size() + 1L;
    }

    private long generateMealId() {

        return patient
                .getMeals()
                .size() + 1L;
    }

    private long generateActivityId() {

        return patient
                .getActivities()
                .size() + 1L;
    }
}