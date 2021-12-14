package com.copropre.common.models;

import com.google.firebase.firestore.Exclude;

import java.util.List;

public class House extends DBClass {
    String houseId;
    String name;
    String description;
    String userCreation;
    String userUpdate;

    @Exclude
    private List<Participant> participants;
    @Exclude
    private List<CPTask> CPTasks;
    @Exclude
    public Participant myParticipant;

    public House() {
    }

    public House(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getHouseId() {
        return houseId;
    }

    public void setHouseId(String houseId) {
        this.houseId = houseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public List<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }

    public List<CPTask> getTasks() {
        return CPTasks;
    }

    public void setTasks(List<CPTask> CPTasks) {
        this.CPTasks = CPTasks;
    }

    @Override
    public String toString() {
        return "House{" +
                "creationDate=" + creationDate +
                ", updateDate=" + updateDate +
                ", houseId='" + houseId + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", userCreation='" + userCreation + '\'' +
                ", userUpdate='" + userUpdate + '\'' +
                ", participants=" + participants +
                ", tasks=" + CPTasks +
                '}';
    }
}
