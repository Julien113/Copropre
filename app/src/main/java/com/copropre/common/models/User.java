package com.copropre.common.models;

public class User extends DBClass{

    String name;
    String mail;
    boolean active;
    boolean sudoer;

    User() {
    }

    public User(String mail, String id, String name) {
        this.mail = mail;
        this.id = id;
        this.name = name;
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
