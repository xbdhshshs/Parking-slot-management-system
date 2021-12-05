package com.licenta.voinescuvlad.voinescuvlad.controllers.dto;

import javax.validation.constraints.*;

public class ApartmentDto {

    private int apartmentID;

    @NotEmpty(message = "Enter woker name")
    @Size(min=1, max=20, message="name must be between 1 and 20 characters")
    private String apartmentName;//worker name

    @NotEmpty(message = "Please enter car type")
    @Pattern(regexp = "^[A-Za-z]+$",message = "Car type composed only from letters.")
    @Size(min=1, max=20, message="Countrey must be between 1 and 20 characters")
    private String countrey;//cartype

    @NotEmpty(message = "Please enter recommended car models")
    @Pattern(regexp = "^[A-Za-z]+$",message = "Models composed only from letters.")
    private String city;

    @NotEmpty(message = "Please enter parking lot alphabet")
    private String adress;//which parking lot?

    @NotNull(message="Please enter price per hour")
    @Min(value = 1,message = "Minimum price is Rs 1/hour")
    @Max(value = 100000,message = "Maximum price is Rs 100000/hour")
    private double ppn;


    private String userName;

    @NotNull(message = "Pleaser enter the service you provide")

    private String service;

    private String status;

    public String getApartmentName() {
        return apartmentName;
    }

    public void setApartmentName(String apartmentName) {
        this.apartmentName = apartmentName;
    }

    public String getCountrey() {
        return countrey;
    }

    public void setCountrey(String countrey) {
        this.countrey = countrey;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public double getPpn() {
        return ppn;
    }

    public void setPpn(double ppn) {
        this.ppn = ppn;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public int getApartmentID() {
        return apartmentID;
    }

    public void setApartmentID(int apartmentID) {
        this.apartmentID = apartmentID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

}
