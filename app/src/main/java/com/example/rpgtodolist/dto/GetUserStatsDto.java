package com.example.rpgtodolist.dto;

public class GetUserStatsDto extends UpdateUserNoHashDto {

    boolean exists;

    public GetUserStatsDto(String name, UserStatsDto value, boolean exists) {
        super(name, value);
        this.exists = exists;
    }

    public boolean isExists() {
        return exists;
    }

    public void setExists(boolean exists) {
        this.exists = exists;
    }
}
