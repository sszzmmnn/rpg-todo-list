package com.example.rpgtodolist.dto;

public class UpdateUserNoHashDto {
    String name;
    UserStatsDto value;

    public UpdateUserNoHashDto(String name, UserStatsDto value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserStatsDto getValue() {
        return value;
    }

    public void setValue(UserStatsDto value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "{\"name\":\"" + name + '\"' + ",\"value\":" + value + '}';
    }
}
