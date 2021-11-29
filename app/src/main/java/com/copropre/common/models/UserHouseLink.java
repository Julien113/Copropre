package com.copropre.common.models;

import java.util.Date;

public class UserHouseLink {

    String userId;
    String houseId;
    Date creationDate;
    boolean active;

    public UserHouseLink() {
    }

    public UserHouseLink(String userId, String houseId, Date creationDate, boolean active) {
        this.userId = userId;
        this.houseId = houseId;
        this.creationDate = creationDate;
        this.active = active;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getHouseId() {
        return houseId;
    }

    public void setHouseId(String houseId) {
        this.houseId = houseId;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }


    @Override
    public String toString() {
        return "UserHouseLink{" +
                "userId='" + userId + '\'' +
                ", houseId='" + houseId + '\'' +
                ", creationDate=" + creationDate +
                ", active=" + active +
                '}';
    }
}
