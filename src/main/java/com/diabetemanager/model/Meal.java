package com.diabetemanager.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Meal {

    private Long id;
    private String name;
    private LocalDateTime dateTime;

    private Long glucoseMeasurementId;

    private List<MealItem> items;

    public Meal(
            Long id,
            String name,
            LocalDateTime dateTime,
            Long glucoseMeasurementId) {

        this.id = id;
        this.name = name;
        this.dateTime = dateTime;
        this.glucoseMeasurementId = glucoseMeasurementId;

        this.items = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public Long getGlucoseMeasurementId() {
        return glucoseMeasurementId;
    }

    public List<MealItem> getItems() {
        return items;
    }

    public void addItem(MealItem item) {
        items.add(item);
    }

    public void removeItem(MealItem item) {
        items.remove(item);
    }

    public double getTotalCarbohydrates() {

        return items.stream()
                .mapToDouble(MealItem::calculateCarbohydrates)
                .sum();
    }

    public double getTotalCalories() {

        return items.stream()
                .mapToDouble(MealItem::calculateCalories)
                .sum();
    }
}