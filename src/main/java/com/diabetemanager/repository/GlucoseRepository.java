package com.diabetemanager.repository;

import com.diabetemanager.model.GlucoseMeasurement;

import java.util.List;

public interface GlucoseRepository {

    List<GlucoseMeasurement> load();

    void save(List<GlucoseMeasurement> measurements);
}