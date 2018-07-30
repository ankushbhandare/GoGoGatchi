package com.gogogatchi.gogogatchi.core;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;
import java.io.Serializable;

public class User implements Serializable {
    private String username;
    private String password;
    private double lat;
    private double longitude;
    private String email;

    public User() {

    }
    private Map<String,Boolean> interests= new HashMap<>();

    //new
    private String userId;
    private String gender;
    private String age;
    private String interest;

    public User(User rightUser) {
        this.userId = rightUser.userId;
        this.username = rightUser.username;
        this.gender = rightUser.gender;
        this.age = rightUser.age;
        this.interest = rightUser.interest;
    }

    public User(String userId, String username, String gender, String age, String interest) {
        this.userId = userId;
        this.username = username;
        this.gender = gender;
        this.age = age;
        this.interest = interest;
    }


    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    //end new

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Map<String, Boolean> getInterests() {
        return interests;
    }

    public void setInterests(Map<String, Boolean> interests) {
        this.interests = interests;
    }

    public double getLongitude() {

        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

}
