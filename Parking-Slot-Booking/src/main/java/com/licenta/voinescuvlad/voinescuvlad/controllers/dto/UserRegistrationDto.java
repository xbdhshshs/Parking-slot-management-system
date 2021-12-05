package com.licenta.voinescuvlad.voinescuvlad.controllers.dto;

import com.licenta.voinescuvlad.voinescuvlad.constraint.FieldMatch;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@FieldMatch.List({
        @FieldMatch(first = "password", second = "passwordConfirmation", message = "The password fields must match"),
        @FieldMatch(first = "email", second = "emailConfirmation", message = "The email fields must match")
})

public class UserRegistrationDto {

    @NotNull
    private int id;

    @NotEmpty
    private String userName;

    @NotEmpty
    @Email(message="Please provide a valid email address")
    private String email;

    @NotEmpty
    @Email(message="Please provide a valid email address")
    private String emailConfirmation;

    @NotEmpty
    private String phone;

    @NotEmpty
    private String carReg;

    @NotEmpty
    private String password;

    @NotEmpty
    private String passwordConfirmation;


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCarReg() {
        return carReg;
    }

    public void setCarReg(String carReg) {
        this.carReg = carReg;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmailConfirmation() {
        return emailConfirmation;
    }

    public void setEmailConfirmation(String emailConfirmation) {
        this.emailConfirmation = emailConfirmation;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }

    public void setPasswordConfirmation(String passwordConfirmation) {
        this.passwordConfirmation = passwordConfirmation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
