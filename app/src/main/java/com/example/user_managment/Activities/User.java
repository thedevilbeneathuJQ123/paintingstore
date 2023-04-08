package com.example.user_managment.Activities;

import com.google.firebase.firestore.DocumentId;

import java.util.Date;

public class User
{
    private String Username;
    private String Location;
    private String Birthday;

    // TODO: add username, birthdate
    public User(String name, String location, String birthday) {
        Username = name;
        Location = location;
        Birthday = birthday;
    }
    public String getLocation() {
        return Location;
    }

    public void setLocation(String Location) {
        this.Location = Location;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String name) {
        Username = name;
    }

    public String getBirthday() {
        return Birthday;
    }

    public void setBirthday(String birthday) {
        Birthday = birthday;
    }

}
