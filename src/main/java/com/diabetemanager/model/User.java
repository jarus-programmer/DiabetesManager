package com.diabetemanager.model;

public abstract class User {

    private Long id;
    private String username;
    private String passwordHash;
    private String firstName;
    private String lastName;
    private Role role;

    public User(
            Long id,
            String username,
            String passwordHash,
            String firstName,
            String lastName,
            Role role) {

        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Role getRole() {
        return role;
    }
}