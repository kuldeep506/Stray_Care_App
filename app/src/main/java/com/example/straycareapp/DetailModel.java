/*
 Created by Intellij IDEA
 Author Name: KULDEEP SINGH (kuldeep506)
 Date: 11-05-2022
*/

package com.example.straycareapp;

public class DetailModel {
    private String animalType;
    private String gender;
    private String description;
    private String address;
    private String city;
    private String senderName;
    private String phoneNumber;

    public DetailModel() {
    }

    public DetailModel(String animalType, String gender, String description,
                       String address, String city, String senderName, String phoneNumber) {
        this.animalType = animalType;
        this.gender = gender;
        this.description = description;
        this.address = address;
        this.city = city;
        this.senderName = senderName;
        this.phoneNumber = phoneNumber;
    }

    public String getAnimalType() {
        return animalType;
    }

    public void setAnimalType(String animalType) {
        this.animalType = animalType;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
