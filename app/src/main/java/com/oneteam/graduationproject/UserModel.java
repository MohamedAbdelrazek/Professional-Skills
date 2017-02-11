package com.oneteam.graduationproject;

/**
 * Created by Mohamed AbdelraZek on 2/11/2017.
 */

public class UserModel {
    private String FirstName;
    private String LastName;
    private String EmailAddress;
    private String MobileNumber;
    private String Password;
    private String reEnterdPassword;
    private String Address;

    public String getFirstName() {
        return FirstName;
    }

    public String getLastName() {
        return LastName;
    }

    public String getEmailAddress() {
        return EmailAddress;
    }

    public String getMobileNumber() {
        return MobileNumber;
    }

    public String getPassword() {
        return Password;
    }

    public String getReEnterdPassword() {
        return reEnterdPassword;
    }

    public String getAddress() {
        return Address;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public void setEmailAddress(String emailAddress) {
        EmailAddress = emailAddress;
    }

    public void setMobileNumber(String mobileNumber) {
        MobileNumber = mobileNumber;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public void setReEnterdPassword(String reEnterdPassword) {
        this.reEnterdPassword = reEnterdPassword;
    }

    public void setAddress(String address) {
        Address = address;
    }
}
