package com.example.rpgtodolist.dto;

public class UserAddSuccessDto {
    boolean added;
    UpdateUserNoHashDto params;

    public UserAddSuccessDto(boolean added, UpdateUserNoHashDto params) {
        this.added = added;
        this.params = params;
    }

    public boolean isAdded() {
        return added;
    }

    public void setAdded(boolean added) {
        this.added = added;
    }

    public UpdateUserNoHashDto getParams() {
        return params;
    }

    public void setParams(UpdateUserNoHashDto params) {
        this.params = params;
    }
}
