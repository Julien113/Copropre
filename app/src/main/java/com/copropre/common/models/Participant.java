package com.copropre.common.models;

import com.google.firebase.firestore.Exclude;

public class Participant extends DBClass {
    String participantId;
    String houseId;
    String userId;
    String surname;
    String userCreation;
    String userUpdate;
    int totalValue;

    @Exclude
    private User user;

    public Participant(String houseId, String userId, String surname, String userCreation, int totalValue) {
        this.houseId = houseId;
        this.userId = userId;
        this.surname = surname;
        this.userCreation = userCreation;
        this.totalValue = totalValue;
    }
    public Participant() {}

    public String getParticipantId() {
        return participantId;
    }

    public void setParticipantId(String participantId) {
        this.participantId = participantId;
    }

    public String getHouseId() {
        return houseId;
    }

    public void setHouseId(String houseId) {
        this.houseId = houseId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserCreation() {
        return userCreation;
    }

    public void setUserCreation(String userCreation) {
        this.userCreation = userCreation;
    }

    public String getUserUpdate() {
        return userUpdate;
    }

    public void setUserUpdate(String userUpdate) {
        this.userUpdate = userUpdate;
    }

    public int getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(int totalValue) {
        this.totalValue = totalValue;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

}
