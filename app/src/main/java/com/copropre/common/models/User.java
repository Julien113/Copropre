package com.copropre.common.models;

import com.google.firebase.firestore.Exclude;

import java.util.List;

public class User extends DBClass{
    String userId;

    String name;
    String mail;
    boolean active;
    boolean sudoer;

    @Exclude
    private List<House> localHouses;

    User() {
    }

    public User(String mail, String id, String name) {
        this.mail = mail;
        this.userId = id;
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isSudoer() {
        return sudoer;
    }

    public void setSudoer(boolean sudoer) {
        this.sudoer = sudoer;
    }
}
