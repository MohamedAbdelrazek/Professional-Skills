package com.oneteam.graduationproject.models;

/**
 * Created by Mohamed AbdelraZek on 2/11/2017.
 */

public class UserModel {
    private String FirstName;
    private String LastName;
    private String EmailAddress;
    private String MobileNumber;
    private String Password;
    private String Address;
    private String Id;
    private String mainSkill;

    public String getMainSkill() {
        return mainSkill;
    }

    public void setMainSkill(String mainSkill) {
        this.mainSkill = mainSkill;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

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

    public void setAddress(String address) {
        Address = address;
    }


}
