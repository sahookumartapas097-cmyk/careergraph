package com.wexa.careergraph.model;

public class Developer {

    private String id;
    private String name;
    private String email;
    private String experience;

    public Developer() {
    }

    public Developer(String id, String name, String email, String experience) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.experience = experience;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }
}