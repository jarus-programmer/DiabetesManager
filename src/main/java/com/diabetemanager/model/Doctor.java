package com.diabetemanager.model;

public class Doctor extends User {

    public Doctor(
            Long id,
            String username,
            String passwordHash,
            String firstName,
            String lastName) {

        super(
                id,
                username,
                passwordHash,
                firstName,
                lastName,
                Role.DOCTOR
        );
    }
}