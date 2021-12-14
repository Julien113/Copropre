package com.copropre.common.models;

public class Occurence extends DBClass {
    // a changer aussi dans TaskService l77 si changement
    String occurenceId;
    String taskId;
    int value;
    String userCreation;
    String userUpdate;
    String participant;

    public Occurence() {
    }

    public Occurence(String taskId, int value, String userCreation, String participant) {
        this.taskId = taskId;
        this.value = value;
        this.userCreation = userCreation;
        this.participant = participant;
    }

    public String getOccurenceId() {
        return occurenceId;
    }

    public void setOccurenceId(String occurenceId) {
        this.occurenceId = occurenceId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
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

    public String getParticipant() {
        return participant;
    }

    public void setParticipant(String participant) {
        this.participant = participant;
    }
}
