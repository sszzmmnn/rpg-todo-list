package com.example.rpgtodolist.dto;

public class UserAddFailureDto {
    boolean added;
    String reason;

    public UserAddFailureDto(boolean added, String reason) {
        this.added = added;
        this.reason = reason;
    }

    public boolean isAdded() {
        return added;
    }

    public void setAdded(boolean added) {
        this.added = added;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
