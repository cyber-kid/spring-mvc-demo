package com.home.entities;

public enum Gender {
    MALE("Male"), FEMALE("Female");

    private String genderDisplayName;

    Gender(String genderDisplayName) {
        this.genderDisplayName = genderDisplayName;
    }

    public String getGenderDisplayName() {
        return genderDisplayName;
    }
}
