package com.copropre.common.models;

import com.google.firebase.firestore.Exclude;

public class Occurrence extends DBClass {
    // a changer aussi dans TaskService l77 si changement
    String occurenceId;
    String taskId;
    String houseId;
    int value;
    String userCreation;
    String userUpdate;
    String participant;

    @Exclude
    CPTask localTask;
    @Exclude
    Participant localParticipant;

    public Occurrence() {
    }

    public Occurrence(String taskId, int value, String userCreation, String houseId, String participant) {
        this.taskId = taskId;
        this.value = value;
        this.houseId = houseId;
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

    public CPTask getLocalTask() {
        return localTask;
    }

    public void setLocalTask(CPTask localTask) {
        this.localTask = localTask;
    }

    public Participant getLocalParticipant() {
        return localParticipant;
    }

    public void setLocalParticipant(Participant localParticipant) {
        this.localParticipant = localParticipant;
    }

    public String getHouseId() {
        return houseId;
    }

    public void setHouseId(String houseId) {
        this.houseId = houseId;
    }


}
